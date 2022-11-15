package uz.androdev.movies.ui.util

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