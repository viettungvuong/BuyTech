package com.tung.buytech

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tung.buytech.AppController.Companion.favorites
import kotlin.math.roundToInt


class SwipeRecyclerHelper(adapter: CartRecyclerAdapter, context: Context): ItemTouchHelper.Callback() {
    lateinit var adapter: CartRecyclerAdapter
    lateinit var context: Context
    init {
        this.adapter=adapter
        this.context=context
    }
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlags)
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

        //removeFromFavorite
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position,favorites.size-position)
        //thông báo với adapter là đã xoá

        //bây giờ xoá phần tử
    }

    //để khi vuốt thì nó sẽ vẽ một phần màu đỏ để bấm xoá
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

            val ratio = (dX/3).roundToInt()

            val background=ColorDrawable()
            background.color=Color.RED
            background.setBounds(itemView.right + ratio, itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            val icon = AppCompatResources.getDrawable(context,R.drawable.remove)

            val intrinsicWidth = icon!!.getIntrinsicWidth();
            val intrinsicHeight = icon!!.getIntrinsicHeight();
            val deleteIconTop: Int = itemView.top + (height - intrinsicHeight) / 2
            val deleteIconMargin: Int = (height - intrinsicHeight) / 2
            val deleteIconLeft: Int = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom: Int = deleteIconTop + intrinsicHeight

            icon.setBounds(deleteIconLeft,deleteIconTop,deleteIconRight,deleteIconBottom)
            icon.setTint(Color.WHITE)
            icon.draw(c)
        }

        //dX/5 nghĩa là ta vuốt 1/5 chiều dài là khoảng nhỏ nhất để nhận sự kiện
        super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive)
    }
}