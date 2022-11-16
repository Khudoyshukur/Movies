package uz.androdev.movies.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import uz.androdev.movies.domain.usecase.ToggleFavoriteMovieUseCase
import java.util.*

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 5:51 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestToggleFavoriteMovieUseCase {
    private lateinit var toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = mock()
        toggleFavoriteMovieUseCase = ToggleFavoriteMovieUseCase(repository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val movieId = UUID.randomUUID().toString()
        toggleFavoriteMovieUseCase.invoke(movieId = movieId)

        Mockito.verify(repository).toggleFavourite(eq(movieId))
    }

    @Test
    fun invoke_whenRepositorySucceed_shouldReturnSuccess() = runTest {
        whenever(repository.toggleFavourite(any()))
            .thenReturn(Unit)

        val movieId = UUID.randomUUID().toString()
        val resp = toggleFavoriteMovieUseCase.invoke(movieId = movieId)

        Assert.assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun invoke_whenRepositoryFails_shouldReturnFailure() = runTest {
        whenever(repository.toggleFavourite(any()))
            .thenThrow(RuntimeException())

        val movieId = UUID.randomUUID().toString()
        val resp = toggleFavoriteMovieUseCase.invoke(movieId = movieId)

        Assert.assertEquals(resp, UseCaseResponse.Failure(UnitFailure))
    }
}