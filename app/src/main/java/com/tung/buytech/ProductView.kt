package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.api.Distribution.BucketOptions.Linear

class ProductView @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context) {

    private val imageView: ImageView
    private val labelTextView: TextView
    private val priceTextView: TextView

    private var button: Button=Button(context)

    private var productName: String=""
    private var productPrice: Long=0

    private val productId: String=""

    init {

        // Inflate the layout for the custom view
        LayoutInflater.from(context).inflate(R.layout.view_product, this, true)

        var layoutParams: LinearLayout.LayoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        button.setBackgroundColor(0) //biến button trong suốt
        button.setOnClickListener(
            View.OnClickListener {
                val intent: Intent= Intent(context,ViewProduct::class.java)
                intent.putExtra("ProductName",productName)
                intent.putExtra("ProductPrice",productPrice)
                intent.putExtra("ProductId",productId)
                context.startActivity(intent) //mở intent
            }
        )

        // Get references to the child views
        imageView = findViewById(R.id.product_image)
        labelTextView = findViewById(R.id.product_label)
        priceTextView = findViewById(R.id.product_price)

        addView(button) //thêm button
    }

    fun setProductImage(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }

    fun setLabel(label: String) {
        productName=label
        labelTextView.text = label
    }

    fun setPrice(price: String) {
        productPrice=price.toLong()
        priceTextView.text = price
    }
}
