package uz.androdev.movies.ui.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by: androdev
 * Date: 16-11-2022
 * Time: 1:31 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class SpaceItemDecoration(private val orientation: Int, private val offset: Int) :
    RecyclerView.ItemDecoration() {

    private var initialOffset = offset
    fun setInitialOffset(initialOffset: Int) {
        this.initialOffset = initialOffset
    }

    init {
        if (orientation !in setOf(HORIZONTAL, VERTICAL)) {
            throw IllegalArgumentException("Please provide a valid orientation")
        } else if (initialOffset < 0) {
            throw IllegalArgumentException("Space cannot be negative number")
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = if (parent.getChildAdapterPosition(view) == 0) {
            initialOffset
        } else {
            offset
        }.dpToPx

        when (orientation) {
            VERTICAL -> {
                outRect.set(0, offset, 0, 0)
            }
            HORIZONTAL -> {
                outRect.set(offset, 0, 0, 0)
            }
        }
    }

    private val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    companion object {
        const val VERTICAL = 0
        const val HORIZONTAL = 1
    }
}