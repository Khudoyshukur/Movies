package uz.androdev.movies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.androdev.movies.data.db.dao.CommentsDao
import uz.androdev.movies.data.db.dao.FavoritesDao
import uz.androdev.movies.data.db.dao.MovieDao
import uz.androdev.movies.data.db.dao.MovieRemoteKeysDao
import uz.androdev.movies.model.entity.CommentEntity
import uz.androdev.movies.model.entity.FavoriteEntity
import uz.androdev.movies.model.entity.MovieEntity
import uz.androdev.movies.model.entity.MovieRemoteKeyEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:43 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Database(
    entities = [
        FavoriteEntity::class,
        CommentEntity::class,
        MovieEntity::class,
        MovieRemoteKeyEntity::class
    ],
    exportSchema = true,
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val favoritesDao: FavoritesDao
    abstract val commentsDao: CommentsDao
    abstract val movieDao: MovieDao
    abstract val movieRemoteKeysDao: MovieRemoteKeysDao

    companion object {
        private const val DATABASE_NAME = "movies.db"

        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}