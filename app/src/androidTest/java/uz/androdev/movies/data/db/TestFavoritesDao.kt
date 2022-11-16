package uz.androdev.movies.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import uz.androdev.movies.data.db.dao.FavoritesDao
import uz.androdev.movies.factory.FavoriteFactory
import java.io.IOException
import java.util.UUID

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 6:43 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestFavoritesDao {
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        favoritesDao = appDatabase.favoritesDao
    }

    @After
    @Throws(IOException::class)
    fun cleanUp() {
        appDatabase.close()
    }

    @Test
    fun insert_shouldInsertData() = runTest {
        val favoriteEntity = with(FavoriteFactory.createFavoriteEntity()) {
            this.copy(
                id = favoritesDao.insertFavorite(this)
            )
        }

        assertTrue(favoritesDao.getFavourite(favoriteEntity.movieId) == favoriteEntity)
    }

    @Test
    fun insert_whenExists_shouldUpdateData() = runTest {
        val favoriteEntity = with(FavoriteFactory.createFavoriteEntity()) {
            this.copy(
                id = favoritesDao.insertFavorite(this)
            )
        }

        val updatedEntity = favoriteEntity.copy(
            movieId = UUID.randomUUID().toString()
        )
        favoritesDao.insertFavorite(updatedEntity)

        assertFalse(favoritesDao.getFavourite(favoriteEntity.movieId) == favoriteEntity)
        assertTrue(favoritesDao.getFavourite(updatedEntity.movieId) == updatedEntity)
    }

    @Test
    fun remove_shouldRemoveAppropriateFavorite() = runTest {
        val favorites = List(10) {
            with(FavoriteFactory.createFavoriteEntity()) {
                val entity = this.copy(movieId = UUID.randomUUID().toString())
                entity.copy(id = favoritesDao.insertFavorite(entity))
            }
        }
        val random = favorites.random()
        favoritesDao.removeFavorite(random.id)

        favorites.filter { it != random }.forEach {
            assertTrue(favoritesDao.getFavourite(it.movieId) != null)
        }
        assertTrue(favoritesDao.getFavourite(random.movieId) == null)
    }

    @Test
    fun getFavorite_shouldReturnAppropriateFavorite() = runTest {
        val favorites = List(10) {
            with(FavoriteFactory.createFavoriteEntity()) {
                this.copy(id = favoritesDao.insertFavorite(this))
            }
        }
        val random = favorites.random()
        assertTrue(favoritesDao.getFavourite(random.movieId) == random)
    }
}