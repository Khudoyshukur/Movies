package uz.androdev.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.androdev.movies.model.entity.FavoriteEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:44 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id=:id")
    suspend fun removeFavorite(id: Long)

    @Query("SELECT * FROM favorites WHERE movie_id=:movieId LIMIT 1")
    suspend fun getFavourite(movieId: String): FavoriteEntity?
}