package uz.androdev.movies.data.db

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uz.androdev.movies.data.db.dao.CommentsDao
import uz.androdev.movies.factory.CommentFactory
import java.io.IOException
import java.util.UUID

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 6:03 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TestCommentDao {
    private lateinit var commentsDao: CommentsDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        commentsDao = appDatabase.commentsDao
    }

    @After
    @Throws(IOException::class)
    fun cleanUp() {
        appDatabase.close()
    }

    @Test
    fun insertComment_shouldInsert() = runTest {
        val comment = with(CommentFactory.createCommentEntity()) {
            this.copy(
                id = commentsDao.insertComment(this)
            )
        }

        val data = commentsDao.getComments(comment.movieId)
        val resp = data.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertTrue((resp as PagingSource.LoadResult.Page).data.contains(comment))
    }

    @Test
    fun numberOfComments_shouldReturnCorrectNumberRespectingMovieId() = runTest {
        val movieId = UUID.randomUUID().toString()
        val comments = List(10) {
            with(CommentFactory.createCommentEntity()) {
                val entity = this.copy(movieId = movieId)
                entity.copy(
                    id = commentsDao.insertComment(entity),
                )
            }
        }

        assertEquals(comments.size, commentsDao.getNumberOfComments(movieId))
        assertEquals(0, commentsDao.getNumberOfComments(movieId + "1"))
    }

    @Test
    fun getComments_shouldReturnCommentsInDatabaseRespectingMovieId() = runTest {
        val movieId = UUID.randomUUID().toString()
        val comments = List(10) { index ->
            with(CommentFactory.createCommentEntity()) {
                val entity = this.copy(
                    id = index + 1L,
                    movieId = movieId
                )
                entity.copy(
                    id = commentsDao.insertComment(entity),
                )
            }
        }

        val data = commentsDao.getComments(movieId)
        val resp = data.load(PagingSource.LoadParams.Refresh(null, comments.size + 10, false))
        assertEquals((resp as PagingSource.LoadResult.Page).data, comments)
    }
}