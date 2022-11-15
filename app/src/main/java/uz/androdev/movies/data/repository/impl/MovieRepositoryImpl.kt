package uz.androdev.movies.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.androdev.movies.data.db.dao.CommentsDao
import uz.androdev.movies.data.db.dao.FavoritesDao
import uz.androdev.movies.data.error.NoInternetException
import uz.androdev.movies.data.error.ServerFailureException
import uz.androdev.movies.data.repository.COMMENTS_PAGE_SIZE
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.data.service.MovieService
import uz.androdev.movies.data.util.ApiResponse
import uz.androdev.movies.di.qualifier.IODispatcher
import uz.androdev.movies.model.entity.CommentEntity
import uz.androdev.movies.model.entity.FavoriteEntity
import uz.androdev.movies.model.mapper.toComment
import uz.androdev.movies.model.mapper.toMovie
import uz.androdev.movies.model.model.Comment
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
    private val favoritesDao: FavoritesDao,
    private val commentsDao: CommentsDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : MovieRepository {
    @Throws(NoInternetException::class, ServerFailureException::class)
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
                        val isLiked = favoritesDao.getFavourite(it.imdbID) != null
                        val numberOfComments = commentsDao.getNumberOfComments(it.imdbID)

                        it.toMovie(
                            isLiked = isLiked,
                            numberOfComments = numberOfComments
                        )
                    }
                }
            }
            is ApiResponse.Exception -> throw NoInternetException()
            is ApiResponse.Failure -> throw ServerFailureException()
        }
    }

    override suspend fun toggleFavourite(movieId: String) {
        val favouriteEntity = favoritesDao.getFavourite(movieId)

        if (favouriteEntity == null) {
            favoritesDao.insertFavorite(
                FavoriteEntity(movieId = movieId)
            )
        } else {
            favoritesDao.removeFavorite(id = favouriteEntity.id)
        }
    }

    override suspend fun insertComment(comment: String, movieId: String) {
        val commentEntity = CommentEntity(
            movieId = movieId,
            comment = comment
        )
        commentsDao.insertComment(commentEntity)
    }

    override fun getComments(movieId: String): Flow<PagingData<Comment>> {
        return Pager(
            PagingConfig(pageSize = COMMENTS_PAGE_SIZE)
        ) {
            commentsDao.getComments(movieId)
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }
}