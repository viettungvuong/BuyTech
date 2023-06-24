package com.tung.buytech

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import java.util.*

class CartRecyclerAdapter(private val itemList: LinkedList<AppController.Product>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_in_cart, //lấy layout item_in_cart làm holder
            parent,
            false
        )
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemInCart: ItemInCart = itemView as ItemInCart
        //itemincart là class chứa view hiện thông tin sản phẩm

        fun bind(product: AppController.Product) {
            itemInCart.setProductImage(product.imageUrl)
            itemInCart.setLabel(product.name)
            itemInCart.setPrice(product.price.toString())
            //gán thông tin từ product vào itemincart
        }
    }
}
