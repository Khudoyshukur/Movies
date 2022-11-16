package uz.androdev.movies.domain

import androidx.paging.PagingData
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
import uz.androdev.movies.domain.usecase.GetMoviesUseCase
import uz.androdev.movies.factory.CommentFactory
import uz.androdev.movies.factory.MovieFactory
import uz.androdev.movies.model.mapper.toComment
import uz.androdev.movies.model.mapper.toMovie
import uz.androdev.movies.model.model.SearchParameter
import java.util.*

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 5:42 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestGetMoviesUseCase {
    private lateinit var getMoviesUseCase: GetMoviesUseCase
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = mock()
        getMoviesUseCase = GetMoviesUseCase(repository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val pagingData = PagingData.from(
            List(10) { MovieFactory.createMovieWithLikeAndNumberOfCommentsEntity().toMovie() }
        )
        whenever(repository.getMovies(any()))
            .thenReturn(flowOf(pagingData))

        val parameter = SearchParameter(UUID.randomUUID().toString(), 5)
        val resp = getMoviesUseCase.invoke(parameter)

        Assert.assertEquals(resp.first(), pagingData)
        Mockito.verify(repository).getMovies(eq(parameter))
    }
}