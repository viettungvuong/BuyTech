package com.tung.buytech

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

    init {
        // Inflate the layout for the custom view
        LayoutInflater.from(context).inflate(R.layout.view_product, this, true)

        // Get references to the child views
        imageView = findViewById(R.id.product_image)
        labelTextView = findViewById(R.id.product_label)
        priceTextView = findViewById(R.id.product_price)
    }

    fun setProductImage(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }

    fun setLabel(label: String) {
        labelTextView.text = label
    }

    fun setPrice(price: String) {
        priceTextView.text = price
    }
}
