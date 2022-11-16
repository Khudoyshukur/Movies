package uz.androdev.movies.factory

import com.github.javafaker.Faker
import org.threeten.bp.LocalDateTime
import uz.androdev.movies.model.entity.CommentEntity

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 4:54 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object CommentFactory {
    private val faker = Faker()

    fun createCommentEntity(): CommentEntity {
        return CommentEntity(
            id = faker.number().randomNumber(),
            movieId = faker.idNumber().valid(),
            content = faker.lorem().characters(),
            createdAt = LocalDateTime.now()
        )
    }
}