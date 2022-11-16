package uz.androdev.movies.model.model

import androidx.recyclerview.widget.DiffUtil
import org.threeten.bp.LocalDateTime

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 8:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class Comment(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}