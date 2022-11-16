package uz.androdev.movies.ui.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import uz.androdev.movies.R
import uz.androdev.movies.ui.constant.UiLayerConstants.MOVIE_PAGE_URL_FORMAT

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

fun Fragment.openMovieInBrowser(movieId: String) {
    val url = String.format(MOVIE_PAGE_URL_FORMAT, movieId)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    try {
        requireContext().startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toastShort(R.string.operation_failed)
    }
}