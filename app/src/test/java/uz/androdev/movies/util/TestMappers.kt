package uz.androdev.movies.util

import org.junit.Assert.assertEquals
import org.junit.Test
import uz.androdev.movies.factory.CommentFactory
import uz.androdev.movies.factory.MovieDetailsFactory
import uz.androdev.movies.factory.MovieFactory
import uz.androdev.movies.model.entity.MovieEntity
import uz.androdev.movies.model.mapper.toComment
import uz.androdev.movies.model.mapper.toMovie
import uz.androdev.movies.model.mapper.toMovieDetails
import uz.androdev.movies.model.mapper.toMovieEntity
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.MovieDetails
import java.util.*

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 4:42 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestMappers {

    @Test
    fun movieDetailsEntity_toMovieDetails_shouldMapCorrectly() {
        val details = MovieDetailsFactory.createMovieDetailsEntity()

        assertEquals(
            details.toMovieDetails(),
            MovieDetails(
                id = details.id,
                title = details.title,
                posterUrl = details.poster,
                releaseYear = details.year,
                isFavorite = details.isFavorite,
                type = details.type
            )
        )
    }

    @Test
    fun movieWithLikeAndCommentEntity_toMovie_shouldMapCorrectly() {
        val entity = MovieFactory.createMovieWithLikeAndNumberOfCommentsEntity()

        assertEquals(
            entity.toMovie(),
            Movie(
                id = entity.id,
                title = entity.title,
                posterUrl = entity.poster,
                releaseYear = entity.year,
                isFavorite = entity.isFavorite,
                type = entity.type,
                numberOfComments = entity.numberOfComments
            )
        )
    }

    @Test
    fun commentEntityToComment_shouldMapCorrectly(){
        val entity = CommentFactory.createCommentEntity()

        assertEquals(
            entity.toComment(),
            Comment(
                id = entity.id,
                content = entity.content,
                createdAt = entity.createdAt,
            )
        )
    }

    @Test
    fun movieDTO_toMovieEntity_shouldMapCorrectly() {
        val dto = MovieFactory.createMovieDTO()

        val query = UUID.randomUUID().toString()
        assertEquals(
            dto.toMovieEntity(query),
            MovieEntity(
                id = dto.imdbID,
                title = dto.title,
                poster = dto.poster,
                type = dto.type,
                year = dto.year,
                query = query
            )
        )
    }
}