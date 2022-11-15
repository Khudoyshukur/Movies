package uz.androdev.movies.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.androdev.movies.data.constant.DataLayerConstants
import uz.androdev.movies.model.dto.SearchResponseDTO

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 6:25 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface MovieService {
    @GET(".")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String = DataLayerConstants.API_KEY,
        @Query("s") query: String,
        @Query("page") page: Int,
    ): Response<SearchResponseDTO>
}