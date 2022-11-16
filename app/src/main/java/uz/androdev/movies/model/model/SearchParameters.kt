package uz.androdev.movies.model.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 11:23 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Parcelize
class SearchParameter(
    val query: String,
    val quantity: Int
) : Parcelable