package uz.androdev.movies.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import uz.androdev.movies.databinding.ItemCommentBinding
import uz.androdev.movies.model.model.Comment

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CommentsAdapter : PagingDataAdapter<Comment, CommentsAdapter.ViewHolder>(Comment.DIFF_UTIL) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position) ?: return
        holder.bind(comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val formatter = DateTimeFormatter.ISO_DATE_TIME

        fun bind(comment: Comment) = with(comment) {
            binding.tvComment.text = content
            binding.tvDatetime.text = formatter.format(createdAt)
        }
    }
}