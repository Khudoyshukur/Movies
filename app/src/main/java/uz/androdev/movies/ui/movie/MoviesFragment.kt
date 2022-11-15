package uz.androdev.movies.ui.movie

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.movies.R
import uz.androdev.movies.databinding.FragmentMoviesBinding
import uz.androdev.movies.model.model.Movie
import uz.androdev.movies.model.model.SearchParameter
import uz.androdev.movies.ui.base.BaseFragment
import uz.androdev.movies.ui.constant.ExtraBundleName.BUNDLE_SEARCH_PARAMETER
import uz.androdev.movies.ui.constant.ExtraRequestKey.KEY_SEARCH_PARAMETERS
import uz.androdev.movies.ui.util.navigateSafely

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
            movies = viewModel.movies,
            processAction = viewModel::processAction
        )
    }

    private fun FragmentMoviesBinding.bindContent(
        movies: Flow<PagingData<Movie>?>,
        processAction: (MoviesAction) -> Unit
    ) {
        bindAppBar()

        bindMovies(
            movies = movies,
            onToggleLike = { processAction(MoviesAction.ToggleLike(it)) }
        )

        bindResultListeners(
            onChangeSearchParameter = {
                processAction(MoviesAction.SetSearchParameter(it))
            }
        )
    }

    private fun FragmentMoviesBinding.bindAppBar() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateSafely(
                MoviesFragmentDirections.actionMoviesFragmentToSearchInputFragment()
            )
        }
    }

    private fun FragmentMoviesBinding.bindMovies(
        movies: Flow<PagingData<Movie>?>,
        onToggleLike: (Movie) -> Unit
    ) {
        val adapter = MoviesAdapter(onToggleLike)
        rvMovies.adapter = adapter

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            launch {
                movies
                    .distinctUntilChanged()
                    .collectLatest{
                        if (it == null) {
                            rvMovies.isVisible = false
                            progressBar.isVisible = false
                            tvMessage.isVisible = true
                            tvMessage.text = getString(R.string.txt_no_search_parameters)
                            return@collectLatest
                        }

                        adapter.submitData(it)
                    }
            }

            launch {
                adapter.loadStateFlow.collectLatest { loadState ->
                    val refreshing = loadState.refresh is LoadState.Loading
                    val isEmpty = loadState.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount == 0
                    val error = loadState.refresh is LoadState.Error

                    rvMovies.isVisible = !isEmpty && !refreshing && !error
                    progressBar.isVisible = refreshing
                    tvMessage.isVisible = isEmpty || error

                    tvMessage.text = when {
                        isEmpty -> getString(R.string.txt_no_movies)
                        error -> getString(R.string.error_occurred)
                        else -> ""
                    }
                }
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