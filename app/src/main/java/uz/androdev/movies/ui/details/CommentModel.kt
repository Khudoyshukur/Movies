package uz.androdev.movies.ui.details

import androidx.recyclerview.widget.DiffUtil
import uz.androdev.movies.model.model.Comment

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 11:31 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed interface CommentModel {
    data class UiComment(val comment: Comment) : CommentModel
    object UiCommentInsertion : CommentModel

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<CommentModel>() {
            override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
                if (oldItem.javaClass != newItem.javaClass) return false

                return when(oldItem){
                    is UiComment -> {
                        Comment.DIFF_UTIL.areItemsTheSame(
                            oldItem.comment,
                            (newItem as UiComment).comment
                        )
                    }
                    UiCommentInsertion -> true
                }
            }

            override fun areContentsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
                if (oldItem.javaClass != newItem.javaClass) return false

                return when(oldItem){
                    is UiComment -> {
                        Comment.DIFF_UTIL.areContentsTheSame(
                            oldItem.comment,
                            (newItem as UiComment).comment
                        )
                    }
                    UiCommentInsertion -> true
                }
            }
        }
    }
}