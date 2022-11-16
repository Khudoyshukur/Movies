package uz.androdev.movies.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
import uz.androdev.movies.domain.usecase.GetMovieDetailsUseCase
import uz.androdev.movies.factory.MovieDetailsFactory
import uz.androdev.movies.model.mapper.toMovieDetails
import java.util.*

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 5:53 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestGetMovieDetailsUseCase {
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = mock()
        getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val movieId = UUID.randomUUID().toString()
        getMovieDetailsUseCase.invoke(movieId = movieId)

        Mockito.verify(repository).getMovieDetails(eq(movieId))
    }

    @Test
    fun invoke_whenRepositorySucceed_shouldReturnSuccess() = runTest {
        val movieDetails = MovieDetailsFactory.createMovieDetailsEntity().toMovieDetails()
        whenever(repository.getMovieDetails(any()))
            .thenReturn(flowOf(movieDetails))

        val movieId = UUID.randomUUID().toString()
        val resp = getMovieDetailsUseCase.invoke(movieId = movieId)

        resp as UseCaseResponse.Success
        Assert.assertEquals(resp.data.first(), movieDetails)
    }

    @Test
    fun invoke_whenRepositoryFails_shouldReturnFailure() = runTest {
        whenever(repository.getMovieDetails(any()))
            .thenThrow(RuntimeException())

        val movieId = UUID.randomUUID().toString()
        val resp = getMovieDetailsUseCase.invoke(movieId = movieId)

        Assert.assertEquals(resp, UseCaseResponse.Failure(UnitFailure))
    }
}