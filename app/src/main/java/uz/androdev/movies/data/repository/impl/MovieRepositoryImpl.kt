package uz.androdev.movies.data.repository.impl

import androidx.paging.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.androdev.movies.data.db.AppDatabase
import uz.androdev.movies.data.pagination.MoviesMediator
import uz.androdev.movies.data.repository.COMMENTS_PAGE_SIZE
import uz.androdev.movies.data.repository.MOVIES_PAGE_SIZE
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.data.service.MovieService
import uz.androdev.movies.di.qualifier.IODispatcher
import uz.androdev.movies.model.entity.CommentEntity
import uz.androdev.movies.model.entity.FavoriteEntity
import uz.androdev.movies.model.mapper.toComment
import uz.androdev.movies.model.mapper.toMovie
import uz.androdev.movies.model.mapper.toMovieDetails
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.MovieDetails
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val appDatabase: AppDatabase,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : MovieRepository {
    override fun getMovies(query: String, quantity: Int): Flow<PagingData<Movie>> {
        val factory = { appDatabase.movieDao.getMovies(query = query) }
        val moviesMediator = MoviesMediator(
            appDatabase = appDatabase,
            movieService = movieService,
            query = query
        )
        val pager = Pager(
            config = PagingConfig(
                pageSize = MOVIES_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = factory,
            remoteMediator = moviesMediator
        )
        return pager.flow.map { pagingData ->
            withContext(dispatcher) {
                pagingData.map { it.toMovie() }
            }
        }
    }

    override fun getMovieDetails(movieId: String): Flow<MovieDetails?> {
        return appDatabase.movieDao.getMoviesDetails(movieId).map {
            withContext(dispatcher) { it?.toMovieDetails() }
        }
    }

    override suspend fun toggleFavourite(movieId: String) {
        val favouriteEntity = appDatabase.favoritesDao.getFavourite(movieId)

        if (favouriteEntity == null) {
            appDatabase.favoritesDao.insertFavorite(
                FavoriteEntity(movieId = movieId)
            )
        } else {
            appDatabase.favoritesDao.removeFavorite(id = favouriteEntity.id)
        }
    }

    override suspend fun insertComment(content: String, movieId: String) {
        val commentEntity = CommentEntity(
            movieId = movieId,
            content = content
        )
        appDatabase.commentsDao.insertComment(commentEntity)
    }

    override fun getComments(movieId: String): Flow<PagingData<Comment>> {
        return Pager(
            PagingConfig(pageSize = COMMENTS_PAGE_SIZE)
        ) {
            appDatabase.commentsDao.getComments(movieId)
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }
}