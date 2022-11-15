package uz.androdev.movies.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.androdev.movies.data.db.AppDatabase
import uz.androdev.movies.data.db.dao.CommentsDao
import uz.androdev.movies.data.db.dao.FavoritesDao
import uz.androdev.movies.data.db.dao.MovieDao
import uz.androdev.movies.data.db.dao.MovieRemoteKeysDao

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:50 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MovieRemoteKeysDaoModule {
    @Provides
    fun provideMovieRemoteKeysDao(appDatabase: AppDatabase): MovieRemoteKeysDao {
        return appDatabase.movieRemoteKeysDao
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MoviesDaoModule {
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FavoritesDaoModule {
    @Provides
    fun provideFavoritesDao(appDatabase: AppDatabase): FavoritesDao {
        return appDatabase.favoritesDao
    }
}

@Module
@InstallIn(SingletonComponent::class)
object CommentsDaoModule {
    @Provides
    fun provideCommentsDao(appDatabase: AppDatabase): CommentsDao {
        return appDatabase.commentsDao
    }
}