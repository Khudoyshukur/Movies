package uz.androdev.movies.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import uz.androdev.movies.data.db.AppDatabase
import uz.androdev.movies.data.repository.impl.MovieRepositoryImpl
import uz.androdev.movies.data.service.MovieService
import uz.androdev.movies.factory.FavoriteFactory
import uz.androdev.movies.factory.MovieDetailsFactory
import uz.androdev.movies.model.mapper.toMovieDetails
import java.util.*

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 4:35 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestMovieRepositoryImpl {
    private lateinit var repository: MovieRepositoryImpl
    private lateinit var movieService: MovieService
    private lateinit var appDatabase: AppDatabase
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        movieService = mock()
        appDatabase = mock()

        whenever(appDatabase.movieDao).thenReturn(mock())
        whenever(appDatabase.commentsDao).thenReturn(mock())
        whenever(appDatabase.favoritesDao).thenReturn(mock())
        whenever(appDatabase.movieRemoteKeysDao).thenReturn(mock())

        dispatcher = UnconfinedTestDispatcher()
        repository = MovieRepositoryImpl(movieService, appDatabase, dispatcher)
    }

    @Test
    fun getMovieDetails_shouldDelegateToDatabase() = runTest {
        val details = MovieDetailsFactory.createMovieDetailsEntity()
        whenever(appDatabase.movieDao.getMoviesDetails(any()))
            .thenReturn(flowOf(details))

        val movieId = "id"
        val resp = repository.getMovieDetails(movieId)

        assertEquals(resp.first(), details.toMovieDetails())
        Mockito.verify(appDatabase.movieDao.getMoviesDetails(eq(movieId)))
    }

    @Test
    fun toggleFavorite_whenAlreadyFavorite_shouldRemoveFavorite() = runTest {
        val favoriteEntity = FavoriteFactory.createFavoriteEntity()

        whenever(appDatabase.favoritesDao.getFavourite(any()))
            .thenReturn(favoriteEntity)

        val movieId = UUID.randomUUID().toString()
        repository.toggleFavourite(movieId)

        Mockito.verify(appDatabase.favoritesDao).removeFavorite(eq(favoriteEntity.id))
    }

    @Test
    fun toggleFavorite_whenNotFavorite_shouldRemoveFavorite() = runTest {
        whenever(appDatabase.favoritesDao.getFavourite(any()))
            .thenReturn(null)

        val movieId = UUID.randomUUID().toString()
        repository.toggleFavourite(movieId)

        Mockito.verify(appDatabase.favoritesDao).insertFavorite(argThat {
            this.movieId == movieId
        })
    }

    @Test
    fun insertComment_shouldDelegateToDatabase() = runTest {
        val content = UUID.randomUUID().toString()
        val movieId = UUID.randomUUID().toString()
        repository.insertComment(content, movieId)

        Mockito.verify(appDatabase.commentsDao).insertComment(
            argThat {
                this.content == content &&
                        this.movieId == movieId
            }
        )
    }

    @Test
    fun getComments_shouldGetCommentsFromDatabase() = runTest {
        val movieId = UUID.randomUUID().toString()
        repository.getComments(movieId)

        Mockito.verify(appDatabase.commentsDao).getComments(eq(movieId))
    }
}