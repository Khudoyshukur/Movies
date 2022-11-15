package uz.androdev.movies.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:41 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(
    tableName = "favorites",
    indices = [
        Index("movie_id", unique = true)
    ]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "movie_id")
    val movieId: String
)