package uz.androdev.movies.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:10 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
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

    @ColumnInfo(name = "search_query")
    val query: String
)