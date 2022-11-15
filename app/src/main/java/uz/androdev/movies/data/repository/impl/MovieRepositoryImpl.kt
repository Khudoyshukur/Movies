package uz.androdev.movies.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.androdev.movies.data.error.NoInternetException
import uz.androdev.movies.data.error.ServerFailureException
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.data.service.MovieService
import uz.androdev.movies.data.util.ApiResponse
import uz.androdev.movies.di.qualifier.IODispatcher
import uz.androdev.movies.model.mapper.toMovie
import uz.androdev.movies.model.model.Movie
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : MovieRepository {
    override suspend fun getMovies(query: String, quantity: Int): List<Movie> {
        val response = ApiResponse.handle {
            movieService.searchMovies(
                query = query
            )
        }

        return when (response) {
            is ApiResponse.Success -> {
                withContext(dispatcher) {
                    response.data.movies.map {
                        it.toMovie(
                            isLiked = false,
                            numberOfComments = 5
                        )
                    }
                }
            }
            is ApiResponse.Exception -> throw NoInternetException()
            is ApiResponse.Failure -> throw ServerFailureException()
        }
    }
}