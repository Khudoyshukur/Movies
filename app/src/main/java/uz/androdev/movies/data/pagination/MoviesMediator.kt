package uz.androdev.movies.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.gson.JsonParseException
import uz.androdev.movies.data.db.AppDatabase
import uz.androdev.movies.data.error.InvalidQueryException
import uz.androdev.movies.data.error.NoInternetException
import uz.androdev.movies.data.service.MovieService
import uz.androdev.movies.model.entity.MovieRemoteKeyEntity
import uz.androdev.movies.model.entity.MovieWithLikeAndCommentEntity
import uz.androdev.movies.model.mapper.toMovieEntity

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 9:08 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalPagingApi::class)
class MoviesMediator(
    private val appDatabase: AppDatabase,
    private val movieService: MovieService,
    private val query: String,
    private val numberOfPages: Int
) : RemoteMediator<Int, MovieWithLikeAndCommentEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: FIRST_PAGE_NUMBER
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prev
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.next
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val movies = movieService.searchMovies(
                query = query,
                page = page
            ).body()!!.movies

            val endOfPagination = page >= numberOfPages || movies.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.movieDao.clearMovies()
                    appDatabase.movieRemoteKeysDao.clearRemoteKeys()
                }
                val prevKey = if (page == FIRST_PAGE_NUMBER) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = movies.map {
                    MovieRemoteKeyEntity(
                        movieId = it.imdbID,
                        prev = prevKey,
                        next = nextKey
                    )
                }
                appDatabase.movieRemoteKeysDao.insertAll(keys)

                val movieEntities = movies.map {
                    it.toMovieEntity(query = query)
                }
                appDatabase.movieDao.insertMovies(movieEntities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: NullPointerException) {
            // caused by !! mark
            // so the query is invalid
            MediatorResult.Error(InvalidQueryException())
        } catch (e: JsonParseException) {
            // server failure
            MediatorResult.Error(InvalidQueryException())
        } catch (e: Throwable) {
            // Most probably connection error
            MediatorResult.Error(NoInternetException())
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item -> appDatabase.movieRemoteKeysDao.remoteKeysMovieId(item.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item -> appDatabase.movieRemoteKeysDao.remoteKeysMovieId(item.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MovieRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { itemId ->
                appDatabase.movieRemoteKeysDao.remoteKeysMovieId(itemId)
            }
        }
    }

    companion object {
        private const val FIRST_PAGE_NUMBER = 1
        private const val TAG = "MoviesMediator"
    }
}

