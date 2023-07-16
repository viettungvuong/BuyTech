package com.tung.buytech

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeRecyclerHelper(adapter: CartRecyclerAdapter): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    lateinit var adapter: CartRecyclerAdapter
    init {
        this.adapter=adapter
    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false //ta kh kich hoat tinh nang keo tha
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        //vi tri adapter position cua mot item trong recycler view

        adapter.notifyItemChanged(position)
        //thông báo với adapter là có thay đổi ở vị trí
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        //nếu ta swipe
        if (actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
            val view = viewHolder.itemView
        }
    }
}