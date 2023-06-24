package com.tung.buytech

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tung.buytech.AppController.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewProductMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)
        val intent = intent
        val s = intent.getStringExtra("ProductName")
        val price = intent.getStringExtra("ProductPrice")
        val productId = intent.getStringExtra("ProductId")
        val productImage = intent.getStringExtra("ProductImage")!!
        val productName: TextView
        val productPrice: TextView
        val productDescription: TextView

        productName = findViewById(R.id.productLabel)
        productPrice = findViewById(R.id.priceLabel)
        productDescription = findViewById(R.id.productDescription)
        productName.text = s
        productPrice.text = price

        //đặt hình ảnh sản phẩm
        val imgView = findViewById<ImageView>(R.id.imageView)
        getImage(productImage, imgView)

        //mô tả sản phẩm
        getDescription(productId, productDescription) //lấy mô tả sản phẩm
        productDescription.textSize = 20f
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.marginStart = 50
        productDescription.layoutParams = layoutParams
        val purchaseBtn = findViewById<Button>(R.id.buttonPurchase)
        val favoriteBtn = findViewById<Button>(R.id.buttonFavorite)
        purchaseBtn.setOnClickListener { v: View? -> }
        favoriteBtn.setOnClickListener { v: View? -> }
    }

    fun getDescription(productId: String?, descriptionText: TextView) { //lấy description
        val db = getDatabaseInstance() //truy cập kotlin từ file java
        val docRef = db.collection(MainActivity.collectionProducts).document(
            productId!!
        )

        // Retrieve the document
        val document = docRef.get() //Task là một dạng asynchronous (ví dụ như Runnable)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    Log.d("Docexists", "DocumentSnapshot data: " + document.data)
                    Log.d(
                        "Docexists2",
                        "DocumentSnapshot data: " + document["description"]
                    )
                    descriptionText.text = document["description"].toString() //set text
                } else {
                    Log.d("Docnodoc", "No such document")
                }
            } else {
                Log.d("Docfailed", "get failed with ", task.exception)
            }
        }
    }

    //đặt hình ảnh
    fun getImage(imageUrl: String, imageView: ImageView) {

        //lấy link ảnh trên storage
        var imageFromStorage = ""
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            try {
                imageFromStorage = AppController.getDownloadUrl(imageUrl)
                // Proceed with the rest of the code, such as creating the `ProductView` instance
                Log.d("ImageUrlSuccess", imageFromStorage)
                Glide.with(this@ViewProductMain)
                    .load(imageFromStorage)
                    .into(imageView)
                // Continue with the rest of the code, e.g., create `ProductView` instance
            } catch (exception: Exception) {
                // Handle the exception if download URL retrieval fails
                println("Error retrieving download URL: ${exception.message}")
            }
        }
    }
}