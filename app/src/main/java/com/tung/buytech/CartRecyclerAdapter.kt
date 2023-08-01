package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tung.buytech.AppController.Companion.findProductImage
import com.tung.buytech.AppController.Companion.reformatNumber
import java.util.*

class CartRecyclerAdapter(val context: Context, private val itemList: LinkedList<out Product>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    //class hiển thị sản phẩm trong cart
    //kế thừa viewHolder của recyclerView
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView
        val labelTextView: TextView
        val priceTextView: TextView
        //mọi thứ trong đây sẽ được bind vào biến view (ta đã inflate trong onCreateViewHolder)

        init{
            imageView = view.findViewById(R.id.product_image)
            labelTextView = view.findViewById(R.id.product_label)
            priceTextView = view.findViewById(R.id.product_price)
        }

        private fun setProductImage(imageUrl: String) {
            findProductImage(imageUrl,imageView,context)
        }

        private fun setLabel(label: String) {
            labelTextView.text = label
        }

        private fun setPrice(price: Long) {
            priceTextView.text = reformatNumber(price).toString()+" VNĐ"
        }

        fun bind(product: Product) {
            //Log.d("Image",product.imageUrl)
            setProductImage(product.imageUrl)
            setLabel(product.name)
            setPrice(product.price)
            //gán thông tin từ product vào ItemInCart (ItemInCart là layout_
            //ItemInCart là layout của holder
        }
    }

    //tạo view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_in_cart, //lấy layout item_in_cart làm view cho adapter
            parent,false
        )
        return CartViewHolder(view) //trả về cart view holder ứng với layout
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = itemList[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem) //gán vào cartViewHolder

        //thêm xử lý khi click
        holder.itemView.setOnClickListener(
            View.OnClickListener {
                //thêm on click Listener
                val product =  itemList[position] //chỗ này thêm copy constructor
                val intent= Intent(context,ViewProductMain::class.java)
                intent.putExtra("Product",product)
                context.startActivity(intent) //mở intent
            }
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }




}
