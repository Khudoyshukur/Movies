package uz.androdev.movies.model.entity

import androidx.room.ColumnInfo

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:35 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class MovieDetailsEntity(
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "year")
    val year: String,

    @ColumnInfo(name = "poster")
    val poster: String?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "is_liked")
    val isLiked: Boolean
)