package uz.androdev.movies.ui.util

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 12:38 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun NavController.navigateSafely(directions: NavDirections) {
    try {
        navigate(directions)
    } catch (_: Exception) {
        // ignore
    }
}

fun Fragment.toastShort(@StringRes resId: Int) {
    Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.toastLong(@StringRes resId: Int) {
    Toast.makeText(requireContext(), resId, Toast.LENGTH_LONG).show()
}