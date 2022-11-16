package uz.androdev.movies.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.insertFooterItem
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.movies.R
import uz.androdev.movies.databinding.FragmentMovieDetailsBinding
import uz.androdev.movies.model.model.Comment
import uz.androdev.movies.model.model.MovieDetails
import uz.androdev.movies.ui.base.BaseFragment
import uz.androdev.movies.ui.util.SpaceItemDecoration
import uz.androdev.movies.ui.util.toastShort

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 10:25 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {
    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        binding.bindContent(
            uiState = viewModel.uiState,
            processAction = viewModel::processAction
        )
    }

    private fun FragmentMovieDetailsBinding.bindContent(
        uiState: StateFlow<MovieDetailsUiState>,
        processAction: (MovieDetailsAction) -> Unit
    ) {
        bindAppBar { findNavController().navigateUp() }

        bindMovieDetails(
            detailsFlow = uiState.map { it.movieDetails },
            onToggleFavorite = {
                processAction(MovieDetailsAction.ToggleFavorite)
            },
            onOpenMovie = {}
        )

        bindComments(
            commentsFlow = uiState.map { it.comments },
            onInsertComment = {
                processAction(MovieDetailsAction.InsertComment(it))
            }
        )

        consumeEffect(
            effect = uiState.map { it.effect },
            onEffectConsumed = {
                processAction(MovieDetailsAction.EffectConsumed)
            }
        )
    }

    private inline fun FragmentMovieDetailsBinding.bindAppBar(
        crossinline onNavigateClicked: () -> Unit
    ) {
        toolbar.setNavigationOnClickListener { onNavigateClicked() }
    }

    private inline fun FragmentMovieDetailsBinding.bindMovieDetails(
        detailsFlow: Flow<MovieDetails?>,
        crossinline onToggleFavorite: () -> Unit,
        crossinline onOpenMovie: (details: MovieDetails) -> Unit,
    ) {
        btnFavorite.setOnClickListener { onToggleFavorite() }

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            detailsFlow.filterNotNull()
                .distinctUntilChanged()
                .collect { details ->
                    Glide.with(requireContext())
                        .load(details.posterUrl)
                        .into(imgPoster)

                    txtTitle.text = details.title
                    txtYear.text = details.releaseYear
                    txtType.text = details.type
                    btnFavorite.isActivated = details.isFavorite

                    txtImdb.setOnClickListener { onOpenMovie(details) }

                    startPostponedEnterTransition()
                }
        }
    }

    private fun FragmentMovieDetailsBinding.bindComments(
        commentsFlow: Flow<PagingData<Comment>?>,
        onInsertComment: (content: String) -> Unit
    ) {
        val commentsAdapter = CommentsAdapter(onInsertComment)
        rvComments.adapter = commentsAdapter
        rvComments.addItemDecoration(
            SpaceItemDecoration(SpaceItemDecoration.VERTICAL, 16).also {
                it.setInitialOffset(6)
            }
        )

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            launch {
                commentsFlow
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collectLatest { data ->
                        val mapper: (Comment) -> CommentModel = { CommentModel.UiComment(it) }
                        val models: PagingData<CommentModel> = data.map(mapper)

                        commentsAdapter.submitData(
                            models.insertFooterItem(item = CommentModel.UiCommentInsertion)
                        )
                    }
            }
        }
    }

    private fun consumeEffect(
        effect: Flow<MovieDetailsEffect?>,
        onEffectConsumed: () -> Unit
    ) {
        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            effect.filterNotNull().collect {
                when (it) {
                    MovieDetailsEffect.ToggleLikeFailed -> {
                        toastShort(R.string.operation_failed)
                    }
                    MovieDetailsEffect.FatalErrorOccurred -> {
                        toastShort(R.string.error_occurred)
                        findNavController().navigateUp()
                    }
                }
                onEffectConsumed()
            }
        }
    }
}