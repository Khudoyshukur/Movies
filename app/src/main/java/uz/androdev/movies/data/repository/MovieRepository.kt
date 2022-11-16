package uz.androdev.movies.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.MovieDetails
import uz.androdev.movies.model.model.SearchParameter
import java.io.IOException

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

const val COMMENTS_PAGE_SIZE = 10
const val MOVIES_PAGE_SIZE = 10

interface MovieRepository {
    fun getMovies(searchParameter: SearchParameter): Flow<PagingData<Movie>>

    fun getMovieDetails(movieId: String): Flow<MovieDetails?>

    @Throws(IOException::class)
    suspend fun toggleFavourite(movieId: String)

    @Throws(IOException::class)
    suspend fun insertComment(content: String, movieId: String)

    fun getComments(movieId: String): Flow<PagingData<Comment>>
}