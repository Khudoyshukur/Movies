package uz.androdev.movies.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:18 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKeyEntity(
    @PrimaryKey val movieId: String,
    val prev: Int? = null,
    val next: Int? = null
)