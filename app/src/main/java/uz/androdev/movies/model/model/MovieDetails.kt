package uz.androdev.movies.model.model

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:32 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class MovieDetails(
    val id: String,
    val title: String,
    val posterUrl: String?,
    val releaseYear: String,
    val type: String,
    val isLiked: Boolean,
)