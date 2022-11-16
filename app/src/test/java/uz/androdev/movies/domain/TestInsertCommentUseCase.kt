package uz.androdev.movies.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import uz.androdev.movies.domain.response.UnitFailure
import uz.androdev.movies.domain.response.UseCaseResponse
import uz.androdev.movies.domain.usecase.InsertCommentUseCase
import java.util.UUID

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 5:46 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestInsertCommentUseCase {
    private lateinit var insertCommentUseCase: InsertCommentUseCase
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = mock()
        insertCommentUseCase = InsertCommentUseCase(repository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val content = UUID.randomUUID().toString()
        val movieId = UUID.randomUUID().toString()
        insertCommentUseCase.invoke(content = content, movieId = movieId)

        Mockito.verify(repository).insertComment(eq(content), eq(movieId))
    }

    @Test
    fun invoke_whenRepositorySucceed_shouldReturnSuccess() = runTest {
        whenever(repository.insertComment(any(), any()))
            .thenReturn(Unit)

        val content = UUID.randomUUID().toString()
        val movieId = UUID.randomUUID().toString()
        val resp = insertCommentUseCase.invoke(content = content, movieId = movieId)

        assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun invoke_whenRepositoryFails_shouldReturnFailure() = runTest {
        whenever(repository.insertComment(any(), any()))
            .thenThrow(RuntimeException())

        val content = UUID.randomUUID().toString()
        val movieId = UUID.randomUUID().toString()
        val resp = insertCommentUseCase.invoke(content = content, movieId = movieId)

        assertEquals(resp, UseCaseResponse.Failure(UnitFailure))
    }
}