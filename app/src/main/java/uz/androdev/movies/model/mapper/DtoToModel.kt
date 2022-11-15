package uz.androdev.movies.model.mapper

import uz.androdev.movies.model.dto.MovieDTO
import uz.androdev.movies.model.model.Movie

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:07 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun MovieDTO.toMovie(isLiked: Boolean, numberOfComments: Int): Movie {
    return Movie(
        id = imdbID,
        title = title,
        posterUrl = poster,
        releaseYear = year,
        type = type,
        isLiked = isLiked,
        numberOfComments = numberOfComments
    )
}