package uz.androdev.movies.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.androdev.movies.databinding.ItemMovieBinding
import uz.androdev.movies.model.model.Movie

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 11:28 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class MoviesAdapter(
    private val onToggleFavorite: (Movie) -> Unit,
    private val onMovieClicked: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviesAdapter.ViewHolder>(Movie.DIFF_UTIL) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie, onMovieClicked, onToggleFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movie: Movie,
            onMovieClicked: (Movie) -> Unit,
            toggleFavorite: (Movie) -> Unit
        ) = with(movie) {
            Glide.with(binding.root)
                .load(posterUrl)
                .into(binding.imgPoster)

            binding.txtTitle.text = title
            binding.txtYear.text = releaseYear
            binding.tctType.text = type
            binding.txtComments.text = numberOfComments.toString()
            binding.btnFavorite.isActivated = isLiked

            binding.root.setOnClickListener { onMovieClicked(movie) }
            binding.btnFavorite.setOnClickListener { toggleFavorite(movie) }
        }
    }
}