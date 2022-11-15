package uz.androdev.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.androdev.movies.model.entity.MovieRemoteKeyEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface MovieRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<MovieRemoteKeyEntity>)

    @Query("SELECT * FROM movie_remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: String): MovieRemoteKeyEntity?

    @Query("DELETE FROM movie_remote_keys")
    suspend fun clearRemoteKeys()
}