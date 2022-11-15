package uz.androdev.movies.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.androdev.movies.data.error.NoInternetException
import uz.androdev.movies.data.error.ServerFailureException
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.Movie
import java.io.IOException

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

const val COMMENTS_PAGE_SIZE = 10

interface MovieRepository {
    @Throws(NoInternetException::class, ServerFailureException::class)
    suspend fun getMovies(query: String, quantity: Int): List<Movie>

    @Throws(IOException::class)
    suspend fun toggleFavourite(movieId: String)

    @Throws(IOException::class)
    suspend fun insertComment(comment: String, movieId: String)

    fun getComments(movieId: String): Flow<PagingData<Comment>>
}