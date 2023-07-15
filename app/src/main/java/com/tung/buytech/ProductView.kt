package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tung.buytech.AppController.Companion.setProductImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductView @JvmOverloads constructor(
    context: Context, product: AppController.Product
) : LinearLayout(context) {

    private val imageView: ImageView
    private val labelTextView: TextView
    private val priceTextView: TextView

    private var productName: String=""
    private var productPrice: Long=0

    private var productId: String=""
    private var imageUrl: String=""


    private var listener: OnClickListener? = null //thêm listener để productView bấm được
    init {

        // Inflate the layout for the custom view
        LayoutInflater.from(context).inflate(R.layout.product_view, this, true)

        val currentProduct = product

        // Get references to the child views
        imageView = findViewById(R.id.product_image)
        labelTextView = findViewById(R.id.product_label)
        priceTextView = findViewById(R.id.product_price)

        productPrice=product.price

        //đặt hình ảnh
        this.imageUrl=imageUrl
        setProductImage(product.imageUrl, imageView, context)

        setPrice(AppController.reformatNumber(product.price)+" VNĐ")
        setId(product.productId)
        setLabel(product.name)

        listener=clickProduct()
    }



    fun setLabel(label: String) {
        productName=label
        labelTextView.text = label
    }

    fun setPrice(price: String) {
        priceTextView.text = price
    }


    fun setId(id: String){
        productId=id
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (listener != null) listener!!.onClick(this)
        }
        return super.dispatchTouchEvent(event)
    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.getAction() === KeyEvent.ACTION_UP && (event.getKeyCode() === KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() === KeyEvent.KEYCODE_ENTER)) {
            if (listener != null) listener!!.onClick(this)
        }
        return super.dispatchKeyEvent(event)
    }

    //tạo onclicklistener
    override fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

    fun clickProduct(): OnClickListener{
        return View.OnClickListener {
            val intent: Intent= Intent(context,ViewProductMain::class.java)
            intent.putExtra("ProductName",productName)
            intent.putExtra("ProductPrice",productPrice)
            intent.putExtra("ProductId",productId)
            intent.putExtra("ProductImage",imageUrl)
            context.startActivity(intent) //mở intent
        }
    }


}

