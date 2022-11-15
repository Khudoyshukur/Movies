package uz.androdev.movies.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.androdev.movies.model.entity.MovieEntity
import uz.androdev.movies.model.entity.MovieWithLikeAndCommentEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:15 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Query(
        """
        SELECT 
        movies.id, movies.title, movies.poster, 
        movies.type, movies.year,
        (
            SELECT COUNT(*) 
            FROM comments 
            WHERE movies.id=comments.movie_id
        ) AS number_of_comments,
        (
            CASE
            WHEN
            (SELECT COUNT(*) FROM favorites WHERE movies.id=favorites.movie_id) > 0
            THEN 1 ELSE 0
            END
        ) AS is_liked
        FROM movies 
        LEFT JOIN favorites
        ON movies.id = favorites.movie_id
        LEFT JOIN comments
        ON movies.id = comments.movie_id
        WHERE search_query=:query
    """
    )
    fun getMovies(query: String): PagingSource<Int, MovieWithLikeAndCommentEntity>
}