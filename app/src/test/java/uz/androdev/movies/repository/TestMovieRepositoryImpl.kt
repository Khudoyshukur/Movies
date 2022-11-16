package uz.androdev.movies.repository

import android.view.View
import android.view.ViewGroup
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
import uz.androdev.movies.factory.CommentFactory
import uz.androdev.movies.factory.FavoriteFactory
import uz.androdev.movies.factory.MovieDetailsFactory
import uz.androdev.movies.model.entity.CommentEntity
import uz.androdev.movies.model.mapper.toComment
import uz.androdev.movies.model.mapper.toMovieDetails
import uz.androdev.movies.model.model.Comment
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
        Mockito.verify(appDatabase.movieDao).getMoviesDetails(eq(movieId))
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
        val commentEntities = List(3) {
            CommentFactory.createCommentEntity()
        }

        val pagingSource = object : PagingSource<Int, CommentEntity>() {
            override fun getRefreshKey(state: PagingState<Int, CommentEntity>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentEntity> {
                return LoadResult.Page(commentEntities, null, null)
            }
        }
        whenever(appDatabase.commentsDao.getComments(any()))
            .thenReturn(pagingSource)

        val movieId = UUID.randomUUID().toString()
        val resp = repository.getComments(movieId)
        val job = launch(UnconfinedTestDispatcher()) {
            resp.collectLatest {
                val comments = it.collectData()
                assertEquals(comments.map { it.id }, commentEntities.map { it.id })
            }
        }
        job.cancel()
    }

    suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, UnconfinedTestDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                onListPresentable()
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }
}