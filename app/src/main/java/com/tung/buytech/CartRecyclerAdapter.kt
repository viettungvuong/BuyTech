package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tung.buytech.AppController.Companion.reformatNumber
import java.util.*

class CartRecyclerAdapter(val context: Context, private val itemList: LinkedList<out AppController.Product>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    //tạo view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_in_cart, //lấy layout item_in_cart làm view
            parent,false
        )
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = itemList[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
        holder.itemView.setOnClickListener(
            View.OnClickListener {
                //thêm on click Listener
                val product =  itemList[position]
                val intent: Intent= Intent(context,ViewProductMain::class.java)
                intent.putExtra("ProductName",product.name)
                intent.putExtra("ProductPrice",product.price)
                intent.putExtra("ProductId",product.productId)
                intent.putExtra("ProductImage",product.imageUrl)
                context.startActivity(intent) //mở intent
            }
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    //class chứa sản phẩm trong cart
    //dùng viewHolder của recyclerView
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView
        val labelTextView: TextView
        val priceTextView: TextView
        //itemincart là class chứa view hiện thông tin sản phẩm

        init{
            imageView = view.findViewById(R.id.product_image)
            labelTextView = view.findViewById(R.id.product_label)
            priceTextView = view.findViewById(R.id.product_price)
        }

        private fun setProductImage(imageUrl: String) {
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)
        }

        private fun setLabel(label: String) {
            labelTextView.text = label
        }

        private fun setPrice(price: Long) {
            priceTextView.text = reformatNumber(price).toString()+" VNĐ"
        }

        fun bind(product: AppController.Product) {
            setProductImage(product.imageUrl)
            setLabel(product.name)
            setPrice(product.price)
            //gán thông tin từ product vào ItemInCart (ItemInCart là layout_
            //ItemInCart là layout của holder
        }
    }

}
