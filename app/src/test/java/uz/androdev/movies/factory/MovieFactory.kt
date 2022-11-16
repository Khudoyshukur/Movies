package uz.androdev.movies.factory

import com.github.javafaker.Faker
import uz.androdev.movies.model.dto.MovieDTO
import uz.androdev.movies.model.entity.MovieEntity
import uz.androdev.movies.model.entity.MovieWithLikeAndCommentEntity

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 4:49 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object MovieFactory {
    private val faker = Faker()

    fun createMovieEntity(): MovieEntity {
        return MovieEntity(
            id = faker.idNumber().valid(),
            type = faker.lorem().characters(),
            year = faker.number().toString(),
            poster = faker.lorem().characters(),
            title = faker.lorem().characters(),
            query = faker.lorem().characters()
        )
    }

    fun createMovieDTO(): MovieDTO {
        return MovieDTO(
            imdbID = faker.idNumber().valid(),
            type = faker.lorem().characters(),
            year = faker.number().toString(),
            poster = faker.lorem().characters(),
            title = faker.lorem().characters(),
        )
    }

    fun createMovieWithLikeAndNumberOfCommentsEntity(): MovieWithLikeAndCommentEntity {
        return MovieWithLikeAndCommentEntity(
            id = faker.idNumber().valid(),
            type = faker.lorem().characters(),
            year = faker.number().toString(),
            poster = faker.lorem().characters(),
            title = faker.lorem().characters(),
            isFavorite = faker.bool().bool(),
            numberOfComments = faker.number().randomDigit()
        )
    }
}