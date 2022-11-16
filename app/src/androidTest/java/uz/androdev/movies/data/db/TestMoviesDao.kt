package uz.androdev.movies.data.db

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uz.androdev.movies.data.db.dao.MovieDao
import uz.androdev.movies.factory.CommentFactory
import uz.androdev.movies.factory.MovieFactory
import uz.androdev.movies.model.entity.FavoriteEntity
import uz.androdev.movies.model.entity.MovieDetailsEntity
import uz.androdev.movies.model.entity.MovieWithLikeAndCommentEntity
import java.io.IOException
import java.util.*

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 6:59 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestMoviesDao {
    private lateinit var moviesDao: MovieDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        moviesDao = appDatabase.movieDao
    }

    @After
    @Throws(IOException::class)
    fun cleanUp() {
        appDatabase.close()
    }

    @Test
    fun insert_shouldInsertData() = runTest {
        val query = UUID.randomUUID().toString()
        val movies = List(5) {
            MovieFactory.createMovieEntity().copy(
                id = UUID.randomUUID().toString(),
                query = query
            )
        }
        moviesDao.insertMovies(movies)

        val data = moviesDao.getMovies(query, movies.size)
        val resp = data.load(PagingSource.LoadParams.Refresh(null, movies.size, false))
        val items = (resp as PagingSource.LoadResult.Page).data
        assertTrue(items.map { it.id } == movies.map { it.id })
    }

    @Test
    fun insert_whenExists_shouldUpdateData() = runTest {
        val movieEntity = MovieFactory.createMovieEntity().copy(
            id = UUID.randomUUID().toString()
        )
        moviesDao.insertMovies(listOf(movieEntity))

        val updatedEntity = movieEntity.copy(
            title = UUID.randomUUID().toString()
        )
        moviesDao.insertMovies(listOf(updatedEntity))

        val data = moviesDao.getMovies(movieEntity.query, 1)
        val resp = data.load(PagingSource.LoadParams.Refresh(null, 1, false))
        val item = (resp as PagingSource.LoadResult.Page).data.first()

        assertFalse(item.title == movieEntity.title)
        assertTrue(item.title == updatedEntity.title)
    }

    @Test
    fun clean_shouldClearAllMovies() = runTest {
        val query = UUID.randomUUID().toString()
        val movies = List(5) {
            MovieFactory.createMovieEntity().copy(
                id = UUID.randomUUID().toString(),
                query = query
            )
        }
        moviesDao.insertMovies(movies)
        moviesDao.clearMovies()

        val data = moviesDao.getMovies(query, movies.size)
        val resp = data.load(PagingSource.LoadParams.Refresh(null, movies.size, false))
        val items = (resp as PagingSource.LoadResult.Page).data
        assertTrue(items.isEmpty())
    }

    @Test
    fun getMovies_shouldReturnFavoriteAndCommentsCount() = runTest {
        val comments = List(10) {
            CommentFactory.createCommentEntity().copy(
                id = 0L // database itself will generate
            )
        }
        val query = UUID.randomUUID().toString()
        val movies = List(10) {
            with(MovieFactory.createMovieEntity().copy(query = query)) {
                moviesDao.insertMovies(listOf(this))

                val isFavorite = it % 2 == 0
                if (isFavorite) {
                    appDatabase.favoritesDao.insertFavorite(
                        FavoriteEntity(movieId = this.id)
                    )
                }

                var commentsSize = 0
                comments.subList(0, (comments.indices).random()).forEach {
                    appDatabase.commentsDao.insertComment(
                        it.copy(movieId = this.id)
                    )
                    commentsSize++
                }

                MovieWithLikeAndCommentEntity(
                    id = this.id,
                    type = this.type,
                    year = this.year,
                    poster = this.poster,
                    title = this.title,
                    isFavorite = isFavorite,
                    numberOfComments = commentsSize
                )
            }
        }

        val data = moviesDao.getMovies(query, movies.size)
        val resp = data.load(PagingSource.LoadParams.Refresh(null, movies.size, false))
        val items = (resp as PagingSource.LoadResult.Page).data
        assertEquals(movies, items)
    }

    @Test
    fun getMovieDetails_shouldReturnAppropriateDataAndItsFavoriteStatus() = runTest {
        val query = UUID.randomUUID().toString()
        val movies = List(10) {
            with(MovieFactory.createMovieEntity().copy(query = query)) {
                moviesDao.insertMovies(listOf(this))

                val isFavorite = it % 2 == 0
                if (isFavorite) {
                    appDatabase.favoritesDao.insertFavorite(
                        FavoriteEntity(movieId = this.id)
                    )
                }

                MovieDetailsEntity(
                    id = this.id,
                    type = this.type,
                    title = this.title,
                    isFavorite = isFavorite,
                    poster = poster,
                    year = year
                )
            }
        }

        val randomMovieDetails = movies.random()
        val data = moviesDao.getMoviesDetails(randomMovieDetails.id).first()
        assertEquals(randomMovieDetails, data)
    }
}