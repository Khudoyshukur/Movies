package uz.androdev.movies.ui.movie

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.movies.R
import uz.androdev.movies.data.error.InvalidQueryException
import uz.androdev.movies.databinding.FragmentMoviesBinding
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.SearchParameter
import uz.androdev.movies.ui.base.BaseFragment
import uz.androdev.movies.ui.constant.ExtraBundleName.BUNDLE_SEARCH_PARAMETER
import uz.androdev.movies.ui.constant.ExtraRequestKey.KEY_SEARCH_PARAMETERS
import uz.androdev.movies.ui.util.SpaceItemDecoration
import uz.androdev.movies.ui.util.navigateSafely
import uz.androdev.movies.ui.util.toastShort

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 10:23 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {
    private val viewModel: MoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindContent(
            uiState = viewModel.uiState,
            processAction = viewModel::processAction
        )
    }

    private fun FragmentMoviesBinding.bindContent(
        uiState: StateFlow<MoviesUiState>,
        processAction: (MoviesAction) -> Unit
    ) {
        bindAppBar(
            title = uiState.map { it.searchParameter?.query },
            onNavigateToSearchInput = {
                val parameter = uiState.value.searchParameter
                findNavController().navigateSafely(
                    MoviesFragmentDirections.actionMoviesFragmentToSearchInputFragment(parameter)
                )
            }
        )

        bindMovies(
            movies = uiState.map { it.movies },
            onToggleFavorite = { processAction(MoviesAction.ToggleFavorite(it)) },
            onMovieClicked = {
                findNavController().navigateSafely(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(it.id)
                )
            }
        )

        collectEffect(
            moviesEffect = uiState.map { it.moviesEffect }.filterNotNull(),
            onEffectConsumed = {
                processAction(MoviesAction.EffectConsumed)
            }
        )

        bindResultListeners(
            onChangeSearchParameter = {
                processAction(MoviesAction.SetSearchParameter(it))
            }
        )
    }

    private inline fun FragmentMoviesBinding.bindAppBar(
        title: Flow<String?>,
        crossinline onNavigateToSearchInput: () -> Unit
    ) {
        toolbar.setNavigationOnClickListener {
            onNavigateToSearchInput()
        }

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            title.distinctUntilChanged().collect {
                toolbar.title = it ?: getString(R.string.movies)
            }
        }
    }

    private fun FragmentMoviesBinding.bindMovies(
        movies: Flow<PagingData<Movie>>,
        onToggleFavorite: (Movie) -> Unit,
        onMovieClicked: (Movie) -> Unit
    ) {
        val adapter = MoviesAdapter(
            onToggleFavorite = onToggleFavorite,
            onMovieClicked = onMovieClicked
        ).also {
            it.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        rvMovies.adapter = adapter

        // Since we are not supporting remove/edit functionalities
        // I am not creating a new ItemAnimator.
        // To remove entire blinking when like clicked,
        // I am setting itemAnimator to null
        rvMovies.itemAnimator = null
        rvMovies.addItemDecoration(
            SpaceItemDecoration(SpaceItemDecoration.VERTICAL, 16)
        )

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            launch {
                movies
                    .distinctUntilChanged()
                    .collectLatest(adapter::submitData)
            }

            launch {
                adapter.loadStateFlow.collectLatest { loadState ->
                    val refreshing = loadState.refresh is LoadState.Loading
                    val error = loadState.refresh is LoadState.Error
                    val isEmpty = loadState.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount == 0

                    rvMovies.isVisible = !isEmpty && !refreshing && !error
                    progressBar.isVisible = refreshing
                    tvMessage.isVisible = error || isEmpty

                    if (isEmpty) {
                        tvMessage.text = getString(R.string.txt_no_search_parameters)
                    } else if (loadState.refresh is LoadState.Error) {
                        val exception = (loadState.refresh as LoadState.Error).error
                        tvMessage.text = when (exception) {
                            is InvalidQueryException -> {
                                getString(R.string.invalid_query)
                            }
                            else -> {
                                getString(R.string.internet_error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectEffect(
        moviesEffect: Flow<MoviesEffect>,
        onEffectConsumed: () -> Unit
    ) {
        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            moviesEffect.collect {
                toastShort(R.string.operation_failed)
                onEffectConsumed()
            }
        }
    }

    private inline fun bindResultListeners(
        crossinline onChangeSearchParameter: (SearchParameter) -> Unit
    ) = with(requireActivity()) {
        supportFragmentManager.setFragmentResultListener(
            KEY_SEARCH_PARAMETERS,
            viewLifecycleOwner
        ) { _, bundle ->
            val searchParameter = bundle.getParcelable<SearchParameter>(BUNDLE_SEARCH_PARAMETER)
            searchParameter?.let { onChangeSearchParameter(it) }
        }
    }
}