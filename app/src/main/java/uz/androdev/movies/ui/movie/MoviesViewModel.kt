package uz.androdev.movies.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import uz.androdev.movies.domain.usecase.GetMoviesUseCase
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
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val searchParameterState = MutableStateFlow<SearchParameter?>(null)
    val movies = searchParameterState.flatMapLatest {
        if (it == null) {
            flowOf(null)
        } else {
            getMoviesUseCase(it.query)
        }
    }

    fun processAction(action: MoviesAction) {
        when (action) {
            is MoviesAction.SetSearchParameter -> {
                viewModelScope.launch { searchParameterState.emit(action.searchParameter) }
            }
            is MoviesAction.ToggleLike -> {}
        }
    }
}

sealed interface MoviesAction {
    data class SetSearchParameter(val searchParameter: SearchParameter) : MoviesAction
    data class ToggleLike(val movie: Movie) : MoviesAction
}