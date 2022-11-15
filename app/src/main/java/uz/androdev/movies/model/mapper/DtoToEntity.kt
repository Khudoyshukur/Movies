package uz.androdev.movies.model.mapper

import uz.androdev.movies.model.dto.MovieDTO
import uz.androdev.movies.model.entity.MovieEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:12 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun MovieDTO.toMovieEntity(query: String): MovieEntity {
    return MovieEntity(
        id = imdbID,
        title = title,
        poster = poster,
        type = type,
        year = year,
        query = query
    )
}