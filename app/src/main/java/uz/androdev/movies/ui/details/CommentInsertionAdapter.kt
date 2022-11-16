package uz.androdev.movies.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.androdev.movies.R
import uz.androdev.movies.databinding.ItemCommentInsertionBinding

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 9:24 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CommentInsertionAdapter(
    private val onInsertComment: (content: String) -> Unit
) : RecyclerView.Adapter<CommentInsertionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentInsertionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(onInsertComment)
    }

    override fun getItemCount() = 1

    class ViewHolder(private val binding: ItemCommentInsertionBinding) :
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
}