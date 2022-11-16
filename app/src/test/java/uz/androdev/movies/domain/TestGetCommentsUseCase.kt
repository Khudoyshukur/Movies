package uz.androdev.movies.domain

import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.domain.usecase.GetCommentsUseCase
import uz.androdev.movies.factory.CommentFactory
import uz.androdev.movies.model.mapper.toComment
import java.util.UUID

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 5:38 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestGetCommentsUseCase {
    private lateinit var getCommentsUseCase: GetCommentsUseCase
    private lateinit var moveRepository: MovieRepository

    @Before
    fun setUp() {
        moveRepository = mock()
        getCommentsUseCase = GetCommentsUseCase(moveRepository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val pagingData = PagingData.from(
            List(10) { CommentFactory.createCommentEntity().toComment() }
        )
        whenever(moveRepository.getComments(any()))
            .thenReturn(flowOf(pagingData))

        val movieId = UUID.randomUUID().toString()
        val resp = getCommentsUseCase.invoke(movieId)

        assertEquals(resp.first(), pagingData)
        Mockito.verify(moveRepository).getComments(eq(movieId))
    }
}