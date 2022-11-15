package uz.androdev.movies.model.model

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:03 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String?,
    val releaseYear: String,
    val isLiked: Boolean,
    val numberOfComments: Int
)