package com.tung.buytech

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import java.util.*

class CartRecyclerAdapter(private val itemList: ArrayList<AppController.Product>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_in_cart,
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

        fun bind(product: AppController.Product) {
            itemInCart.setProductImage(product.imageFile)
            itemInCart.setLabel(product.name)
            itemInCart.setPrice(product.price.toString())
        }
    }
}
