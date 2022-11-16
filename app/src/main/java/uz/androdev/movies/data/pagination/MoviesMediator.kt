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
    private val query: String
) : RemoteMediator<Int, MovieWithLikeAndCommentEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with `endOfPaginationReached = false` because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                    // the end of pagination for prepend.
                    val prevKey = remoteKeys?.prev
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with `endOfPaginationReached = false` because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey = remoteKeys?.next
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val movies = movieService.searchMovies(
                query = query,
                page = page + 1
            ).body()!!.movies

            val endOfPagination = movies.isEmpty()

            appDatabase.withTransaction {
                // clear articles from table
                if (loadType == LoadType.REFRESH) {
                    appDatabase.movieDao.clearMovies()
                    appDatabase.movieRemoteKeysDao.clearRemoteKeys()
                }
                val prevKey = if (page == 0) null else page - 1
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
            MediatorResult.Error(NoInternetException())
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MovieRemoteKeyEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                // Get the remote keys of the last item retrieved
                appDatabase.movieRemoteKeysDao.remoteKeysMovieId(item.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MovieRemoteKeyEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                // Get the remote keys of the first items retrieved
                appDatabase.movieRemoteKeysDao.remoteKeysMovieId(item.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieWithLikeAndCommentEntity>
    ): MovieRemoteKeyEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { itemId ->
                appDatabase.movieRemoteKeysDao.remoteKeysMovieId(itemId)
            }
        }
    }
}

