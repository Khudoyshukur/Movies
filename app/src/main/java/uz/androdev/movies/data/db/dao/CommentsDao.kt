package uz.androdev.movies.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.androdev.movies.model.entity.CommentEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 8:13 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface CommentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentEntity: CommentEntity)

    @Query("SELECT COUNT(*) FROM comments WHERE movie_id=:movieId")
    suspend fun getNumberOfComments(movieId: String): Int

    @Query("SELECT * FROM comments WHERE movie_id=:movieId")
    fun getComments(movieId: String): PagingSource<Int, CommentEntity>
}