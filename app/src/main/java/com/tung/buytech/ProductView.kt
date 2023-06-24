package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine


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
        LayoutInflater.from(context).inflate(R.layout.view_product, this, true)

        val currentProduct = product

        // Get references to the child views
        imageView = findViewById(R.id.product_image)
        labelTextView = findViewById(R.id.product_label)
        priceTextView = findViewById(R.id.product_price)

        productPrice=product.price

        setProductImage(product.imageUrl)
        setPrice(AppController.reformatNumber(product.price)+" VNĐ")
        setId(product.productId)
        setLabel(product.name)

        listener=clickProduct()
    }

    fun setProductImage(imageUrl: String) {
        this.imageUrl=imageUrl

        //lấy link ảnh trên storage
        var imageFromStorage = ""
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            try {
                imageFromStorage = AppController.getDownloadUrl(imageUrl)
                // Proceed with the rest of the code, such as creating the `ProductView` instance
                Log.d("ImageUrlSuccess", imageFromStorage)
                Glide.with(context)
                    .load(imageFromStorage)
                    .into(imageView)
                // Continue with the rest of the code, e.g., create `ProductView` instance
            } catch (exception: Exception) {
                // Handle the exception if download URL retrieval fails
                println("Error retrieving download URL: ${exception.message}")
            }
        }

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

