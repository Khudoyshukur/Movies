package uz.androdev.movies.ui.movie.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.androdev.movies.R
import uz.androdev.movies.databinding.FragmentSearchInputBinding
import uz.androdev.movies.model.model.SearchParameter
import uz.androdev.movies.ui.constant.ExtraBundleName
import uz.androdev.movies.ui.constant.ExtraBundleName.BUNDLE_SEARCH_PARAMETER
import uz.androdev.movies.ui.constant.ExtraRequestKey
import uz.androdev.movies.ui.constant.ExtraRequestKey.KEY_SEARCH_PARAMETERS

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 11:58 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class SearchInputFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSearchInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnApply.setOnClickListener {
            binding.applySearchParameters()
        }
    }

    private fun FragmentSearchInputBinding.applySearchParameters() {
        val query = inputTitle.editText?.text
        if (query.isNullOrBlank()) {
            inputTitle.error = getString(R.string.fill_field)
            return
        }

        val quantity = inputQuantity.editText?.text.toString().toIntOrNull()
        if (quantity == null) {
            inputQuantity.error = getString(R.string.enter_valid_number)
            return
        }

        val result = bundleOf(
            BUNDLE_SEARCH_PARAMETER to SearchParameter(
                query = query.toString(),
                quantity = quantity
            )
        )
        activity?.supportFragmentManager?.setFragmentResult(KEY_SEARCH_PARAMETERS, result)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}