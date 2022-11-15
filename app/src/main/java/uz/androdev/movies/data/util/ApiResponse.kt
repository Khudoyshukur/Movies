package uz.androdev.movies.data.util

import retrofit2.Response

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:14 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed class ApiResponse<out T : Any> {
    data class Success<T : Any>(val data: T) : ApiResponse<T>()
    data class Failure(val errorCode: Int, val errorMessage: String) : ApiResponse<Nothing>()
    data class Exception(val exception: kotlin.Exception) : ApiResponse<Nothing>()

    companion object {
        suspend fun <T: Any> handle(request: suspend () -> Response<T>): ApiResponse<T> {
            return try {
                val response = request()

                if (response.isSuccessful) {
                    Success(response.body()!!)
                } else {
                    Failure(response.code(), response.message())
                }
            } catch (e: kotlin.Exception) {
                e.printStackTrace()
                Exception(e)
            }
        }
    }
}