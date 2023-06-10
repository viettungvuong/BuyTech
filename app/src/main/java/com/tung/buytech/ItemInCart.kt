package com.tung.buytech

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class ItemInCart @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context) {

    private val imageView: ImageView
    private val labelTextView: TextView
    private val priceTextView: TextView

    init {
        // Inflate the layout for the custom view
        LayoutInflater.from(context).inflate(R.layout.item_in_cart, this, true)

        // Get references to the child views
        imageView = findViewById(R.id.product_image)
        labelTextView = findViewById(R.id.product_label)
        priceTextView = findViewById(R.id.product_price)
    }

}