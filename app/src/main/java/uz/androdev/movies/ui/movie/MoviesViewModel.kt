package uz.androdev.movies.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.movies.domain.response.onFailure
import uz.androdev.movies.domain.usecase.GetMoviesUseCase
import uz.androdev.movies.domain.usecase.ToggleFavoriteMovieUseCase
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.SearchParameter
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 11:18 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {
    private val searchParameterState = MutableStateFlow<SearchParameter?>(null)
    private val effect = MutableStateFlow<Effect?>(null)
    val movies = searchParameterState.flatMapLatest {
        if (it == null) {
            flowOf(PagingData.empty())
        } else {
            getMoviesUseCase(it.query)
        }
    }.cachedIn(viewModelScope)

    val uiState = combine(
        movies, effect, searchParameterState, ::MoviesUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MoviesUiState()
    )

    fun processAction(action: MoviesAction) {
        when (action) {
            is MoviesAction.SetSearchParameter -> searchParameterState.update { action.searchParameter }
            is MoviesAction.ToggleFavorite -> toggleFavorite(action.movie)
            MoviesAction.EffectConsumed -> effect.update { null }
        }
    }

    private fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val resp = toggleFavoriteMovieUseCase(movie.id)

            resp.onFailure {
                effect.emit(Effect.ToggleLikeFailed)
            }
        }
    }
}

data class MoviesUiState(
    val movies: PagingData<Movie> = PagingData.empty(),
    val effect: Effect? = null,
    val searchParameter: SearchParameter? = null
)

sealed interface MoviesAction {
    data class SetSearchParameter(val searchParameter: SearchParameter) : MoviesAction
    data class ToggleFavorite(val movie: Movie) : MoviesAction
    object EffectConsumed : MoviesAction
}

sealed interface Effect {
    object ToggleLikeFailed : Effect
}