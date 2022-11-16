package uz.androdev.movies.factory

import com.github.javafaker.Faker
import uz.androdev.movies.model.entity.FavoriteEntity

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 5:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FavoriteFactory {
    private val faker = Faker()

    fun createFavoriteEntity(): FavoriteEntity {
        return FavoriteEntity(
            id = faker.number().randomNumber(),
            movieId = faker.lorem().characters()
        )
    }
}