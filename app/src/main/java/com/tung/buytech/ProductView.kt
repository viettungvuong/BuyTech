package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide


class ProductView @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context) {

    private val imageView: ImageView
    private val labelTextView: TextView
    private val priceTextView: TextView

    private var productName: String=""
    private var productPrice: String=""

    private var productId: String=""
    private var listener: OnClickListener? = null

    private var imageUrl: String=""
    init {

        // Inflate the layout for the custom view
        LayoutInflater.from(context).inflate(R.layout.view_product, this, true)

        // Get references to the child views
        imageView = findViewById(R.id.product_image)
        labelTextView = findViewById(R.id.product_label)
        priceTextView = findViewById(R.id.product_price)

        listener=clickProduct()
    }

    fun setProductImage(imageUrl: String) {
        this.imageUrl=imageUrl
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }

    fun setLabel(label: String) {
        productName=label
        labelTextView.text = label
    }

    fun setPrice(price: String) {
        productPrice=price
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

    override fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

    fun clickProduct(): OnClickListener{
        return View.OnClickListener {
            val intent: Intent= Intent(context,ViewProduct::class.java)
            intent.putExtra("ProductName",productName)
            intent.putExtra("ProductPrice",productPrice)
            intent.putExtra("ProductId",productId)
            intent.putExtra("ProductImage",imageUrl)
            context.startActivity(intent) //mở intent
        }
    }
}
