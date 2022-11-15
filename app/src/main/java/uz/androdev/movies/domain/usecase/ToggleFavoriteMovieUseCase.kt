package uz.androdev.movies.domain.usecase

import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.domain.response.UnitFailure
import uz.androdev.movies.domain.response.UseCaseResponse
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 12:59 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class ToggleFavoriteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: String): UseCaseResponse<Unit, UnitFailure> {
        return try {
            movieRepository.toggleFavourite(movieId)
            UseCaseResponse.Success(Unit)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UnitFailure)
        }
    }
}