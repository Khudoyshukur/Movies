package uz.androdev.movies.model.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:48 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class MovieWithLikeAndCommentEntity(
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

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,

    @ColumnInfo(name = "number_of_comments")
    val numberOfComments: Int
)