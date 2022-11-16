package uz.androdev.movies.factory

import com.github.javafaker.Faker
import uz.androdev.movies.model.entity.MovieDetailsEntity

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 4:38 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object MovieDetailsFactory {
    private val faker = Faker()

    fun createMovieDetailsEntity(): MovieDetailsEntity {
        return MovieDetailsEntity(
            id = faker.idNumber().valid(),
            type = faker.lorem().characters(),
            year = faker.number().toString(),
            poster = faker.lorem().characters(),
            title = faker.lorem().characters(),
            isFavorite = faker.bool().bool()
        )
    }
}