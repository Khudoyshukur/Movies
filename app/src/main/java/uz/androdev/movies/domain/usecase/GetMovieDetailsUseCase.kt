package uz.androdev.movies.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.domain.response.UnitFailure
import uz.androdev.movies.domain.response.UseCaseResponse
import uz.androdev.movies.model.model.MovieDetails
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:30 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId: String): UseCaseResponse<Flow<MovieDetails>, UnitFailure> {
        return try {
            val details = movieRepository.getMovieDetails(movieId).map { it!! }
            UseCaseResponse.Success(details)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UnitFailure)
        }
    }
}