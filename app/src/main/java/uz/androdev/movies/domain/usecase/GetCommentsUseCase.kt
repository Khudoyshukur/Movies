package uz.androdev.movies.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.model.model.Comment
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:40 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetCommentsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId: String): Flow<PagingData<Comment>> {
        return movieRepository.getComments(movieId = movieId)
    }
}