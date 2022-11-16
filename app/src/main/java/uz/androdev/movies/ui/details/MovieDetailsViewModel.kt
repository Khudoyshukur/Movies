package uz.androdev.movies.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.movies.domain.response.onFailure
import uz.androdev.movies.domain.response.onSuccess
import uz.androdev.movies.domain.usecase.GetCommentsUseCase
import uz.androdev.movies.domain.usecase.GetMovieDetailsUseCase
import uz.androdev.movies.domain.usecase.InsertCommentUseCase
import uz.androdev.movies.domain.usecase.ToggleFavoriteMovieUseCase
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.MovieDetails
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:16 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val insertCommentsUseCase: InsertCommentUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = MovieDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val movieDetailsFlow = MutableStateFlow<MovieDetails?>(null)
    private val effectsState = MutableStateFlow<MovieDetailsEffect?>(null)

    val uiState = combine(
        movieDetailsFlow,
        getCommentsUseCase(args.movieId).cachedIn(viewModelScope),
        effectsState,
        ::MovieDetailsUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieDetailsUiState()
    )

    init {
        val detailsResponse = getMovieDetailsUseCase(args.movieId)

        detailsResponse.onFailure {
            effectsState.update { MovieDetailsEffect.FatalErrorOccurred }
        }

        detailsResponse.onSuccess {
            viewModelScope.launch {
                it.collectLatest(movieDetailsFlow::emit)
            }
        }
    }

    fun processAction(action: MovieDetailsAction) {
        when (action) {
            is MovieDetailsAction.InsertComment -> insertComment(action.content)
            MovieDetailsAction.ToggleFavorite -> toggleFavorite()
            MovieDetailsAction.EffectConsumed -> effectsState.update { null }
        }
    }

    private fun insertComment(content: String) {
        viewModelScope.launch {
            insertCommentsUseCase(content = content, movieId = args.movieId)
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(args.movieId)
        }
    }
}

data class MovieDetailsUiState(
    val movieDetails: MovieDetails? = null,
    val comments: PagingData<Comment>? = null,
    val effect: MovieDetailsEffect? = null
)

sealed interface MovieDetailsAction {
    object ToggleFavorite : MovieDetailsAction
    object EffectConsumed : MovieDetailsAction
    data class InsertComment(val content: String) : MovieDetailsAction
}

sealed interface MovieDetailsEffect {
    object ToggleLikeFailed : MovieDetailsEffect
    object FatalErrorOccurred : MovieDetailsEffect
}