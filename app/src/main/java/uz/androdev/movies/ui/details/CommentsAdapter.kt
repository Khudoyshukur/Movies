package uz.androdev.movies.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import uz.androdev.movies.R
import uz.androdev.movies.databinding.ItemCommentBinding
import uz.androdev.movies.databinding.ItemCommentInsertionBinding
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.ui.constant.UiLayerConstants.COMMENT_DATE_TIME_FORMAT

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CommentsAdapter(
    private val onInsertComment: (content: String) -> Unit
) : PagingDataAdapter<CommentModel, RecyclerView.ViewHolder>(CommentModel.DIFF_UTIL) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position) ?: return

        when (model) {
            is CommentModel.UiComment -> {
                (holder as CommentViewHolder).bind(model.comment)
            }
            CommentModel.UiCommentInsertion -> {
                (holder as CommentInsertionViewHolder).bind(onInsertComment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COMMENT -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCommentBinding.inflate(inflater, parent, false)
                CommentViewHolder(binding)
            }
            VIEW_TYPE_COMMENT_INSERTION -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCommentInsertionBinding.inflate(inflater, parent, false)
                CommentInsertionViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val formatter = DateTimeFormatter.ofPattern(COMMENT_DATE_TIME_FORMAT)

        fun bind(comment: Comment) = with(comment) {
            binding.tvComment.text = content
            binding.tvDatetime.text = formatter.format(createdAt)
        }
    }

    class CommentInsertionViewHolder(private val binding: ItemCommentInsertionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(onInsertComment: (content: String) -> Unit) = with(binding) {
            btnApply.setOnClickListener {
                val content = inputComment.editText?.text

                if (content.isNullOrBlank()) {
                    inputComment.error = root.resources.getString(R.string.fill_field)
                    return@setOnClickListener
                }

                onInsertComment(content.toString())

                inputComment.error = null
                inputComment.editText?.setText("")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CommentModel.UiComment -> VIEW_TYPE_COMMENT
            CommentModel.UiCommentInsertion -> VIEW_TYPE_COMMENT_INSERTION
            null -> VIEW_TYPE_COMMENT
        }
    }

    companion object {
        private const val VIEW_TYPE_COMMENT = 1
        private const val VIEW_TYPE_COMMENT_INSERTION = 2
    }
}