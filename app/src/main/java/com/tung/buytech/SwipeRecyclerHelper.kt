package com.tung.buytech

import android.content.Context
import android.graphics.*
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tung.buytech.AppController.Companion.favorites


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

            val p = Paint()
            p.color= Color.RED
            val background = RectF(itemView.right.toFloat() + dX/3, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            c.drawRect(background, p) //đặt màu đỏ
            val icon = AppCompatResources.getDrawable(context,R.drawable.remove)
            print("Icon:"+icon)
            val margin = (dX / 5 - width) / 2
            val iconDest = RectF(itemView.right.toFloat() + margin, itemView.top.toFloat() + width, itemView.right.toFloat() + (margin + width), itemView.bottom.toFloat() - width)
            icon!!.draw(c)
        }

        //dX/5 nghĩa là ta vuốt 1/5 chiều dài là khoảng nhỏ nhất để nhận sự kiện
        super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive)
    }
}