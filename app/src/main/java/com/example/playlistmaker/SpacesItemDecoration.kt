package com.example.playlistmaker

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// пока что не использовал этот класс в коде, но может пригодиться я думаю
class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            left = space
            right = space
            bottom = space


            if (parent.getChildLayoutPosition(view) == 0) {
                top = space
            } else {
                top = 0
            }
        }
    }
}