package uz.androdev.movies.model.entity

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 8:10 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(
    tableName = "comments",
    indices = [
        Index("movie_id", unique = false)
    ]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "movie_id")
    val movieId: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        val DIFF_UTIL = object : ItemCallback<CommentEntity>() {
            override fun areItemsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CommentEntity,
                newItem: CommentEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}