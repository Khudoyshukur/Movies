package uz.androdev.movies.model.mapper

import uz.androdev.movies.model.entity.CommentEntity
import uz.androdev.movies.model.entity.MovieEntity
import uz.androdev.movies.model.entity.MovieWithLikeAndCommentEntity
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.Movie

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 8:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun CommentEntity.toComment(): Comment {
    return Comment(
        id = id,
        comment = comment,
        createdAt = createdAt
    )
}

fun MovieWithLikeAndCommentEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = poster,
        releaseYear = year,
        isLiked = isLiked,
        numberOfComments = numberOfComments
    )
}