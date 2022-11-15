package uz.androdev.movies.model.model

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:03 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String?,
    val releaseYear: String,
    val type: String,
    val isLiked: Boolean,
    val numberOfComments: Int
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}