package uz.androdev.movies.data.repository

import uz.androdev.movies.data.error.NoInternetException
import uz.androdev.movies.data.error.ServerFailureException
import uz.androdev.movies.model.model.Movie

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface MovieRepository {
    @Throws(NoInternetException::class, ServerFailureException::class)
    suspend fun getMovies(query: String, quantity: Int): List<Movie>
}