package uz.androdev.movies.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.SearchParameter
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 10:16 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(searchParameter: SearchParameter): Flow<PagingData<Movie>> {
        return movieRepository.getMovies(searchParameter)
    }
}