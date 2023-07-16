package com.tung.buytech

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeRecyclerHelper(adapter: CartRecyclerAdapter, context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    lateinit var adapter: CartRecyclerAdapter
    lateinit var context: Context
    init {
        this.adapter=adapter
        this.context=context
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
            val itemView = viewHolder.itemView
            val height = itemView.height
            val width = itemView.width

            val p = Paint()
            p.color= Color.RED
            val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            c.drawRect(background, p) //đặt màu đỏ
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.remove)
            val margin = (dX / 5 - width) / 2
            val iconDest = RectF(itemView.right.toFloat() + margin, itemView.top.toFloat() + width, itemView.right.toFloat() + (margin + width), itemView.bottom.toFloat() - width)
            c.drawBitmap(icon, null, iconDest, p)
        }

        //dX/5 nghĩa là ta vuốt 1/5 chiều dài là khoảng nhỏ nhất để nhận sự kiện
        super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive)
    }
}