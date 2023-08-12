package com.tung.buytech

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tung.buytech.adapters.CartRecyclerAdapter
import com.tung.buytech.control.AppController
import com.tung.buytech.objects.Product
import java.util.LinkedList
import kotlin.math.roundToInt

class CartPage
    : Fragment() {

    lateinit var swipeRecyclerHelper: SwipeRecyclerHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.cart, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.cartView)

        // Set recyclerView in LinearLayoutManager
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = layoutManager
        val adapter = CartRecyclerAdapter(requireContext(), AppController.cart.cartList)
        swipeRecyclerHelper=SwipeRecyclerHelper(adapter,requireContext())

        recyclerView.adapter = adapter
        // Assign the adapter to the recyclerView


        return rootView
    }

    fun deleteFromCart(itemInCart: Product, cart: LinkedList<Product>) {
        if (!cart.contains(itemInCart))
            return

        cart.removeAt(cart.indexOf(itemInCart))
    }
}

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
        AppController.removeFavorite(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, AppController.favorites.size-position)
        viewHolder.itemView.visibility= View.GONE
        Toast.makeText(
            context,
            "Đã xoá khỏi danh sách yêu thích",
            Toast.LENGTH_SHORT,
        ).show()
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
        if (actionState== ItemTouchHelper.ACTION_STATE_SWIPE){
            val itemView = viewHolder.itemView //view holder
            val height = itemView.height //chiều cao item view
            val ratio = (dX/6).roundToInt()

            val background= ColorDrawable()
            background.color= Color.parseColor("#d4798c")
            background.setBounds(itemView.right + ratio, itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            val icon = AppCompatResources.getDrawable(context,R.drawable.remove)

            val intrinsicWidth = icon!!.getIntrinsicWidth()
            val intrinsicHeight = icon!!.getIntrinsicHeight()

            val deleteIconMargin: Int = (height - intrinsicHeight) / 2
            //đảm bảo nút xoá ở giữa phần swipe

            val pushToRight = 95
            val deleteIconTop: Int = itemView.top + deleteIconMargin
            val deleteIconLeft: Int = itemView.right - deleteIconMargin - intrinsicWidth + pushToRight
            val deleteIconRight = itemView.right - deleteIconMargin + pushToRight //đưa nút trừ hiện ra sớm hơn
            val deleteIconBottom: Int = deleteIconTop + intrinsicHeight

            icon.setBounds(deleteIconLeft,deleteIconTop,deleteIconRight,deleteIconBottom)
            icon.setTint(Color.WHITE)
            icon.draw(c)
        }

        //dX/5 nghĩa là ta vuốt 1/5 chiều dài là khoảng nhỏ nhất để nhận sự kiện
        super.onChildDraw(c, recyclerView, viewHolder, dX/6, dY, actionState, isCurrentlyActive)
    }
}
