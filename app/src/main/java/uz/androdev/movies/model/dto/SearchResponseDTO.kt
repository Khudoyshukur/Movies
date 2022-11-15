package uz.androdev.movies.model.dto

import com.google.gson.annotations.SerializedName

data class SearchResponseDTO(
	@field:SerializedName("Response")
	val response: String,

	@field:SerializedName("totalResults")
	val totalResults: Int = 0,

	@field:SerializedName("Search")
	val movies: List<MovieDTO>
)

data class MovieDTO(
	@field:SerializedName("Type")
	val type: String,

	@field:SerializedName("Year")
	val year: String,

	@field:SerializedName("imdbID")
	val imdbID: String,

	@field:SerializedName("Poster")
	val poster: String?,

	@field:SerializedName("Title")
	val title: String
)
