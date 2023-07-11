package com.tung.buytech

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.tung.buytech.AppController.Companion.addToFavorite
import com.tung.buytech.AppController.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewProductMain : AppCompatActivity() {
    companion object{
        @JvmField
        var currentProduct: AppController.Product?=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)

        val intent = intent
        val name = intent.getStringExtra("ProductName")!!
        val price = intent.getLongExtra("ProductPrice",0)
        val productId = intent.getStringExtra("ProductId")!!
        val productImage = intent.getStringExtra("ProductImage")!!

        //tạo product tương ứng
        currentProduct=AppController.Product(name,price,productImage,productId)

        val productName: TextView
        val productPrice: TextView
        val productDescription: TextView

        productName = findViewById(R.id.productLabel)
        productPrice = findViewById(R.id.priceLabel)
        productDescription = findViewById(R.id.productDescription)
        productName.text = name
        productPrice.text = AppController.reformatNumber(price)+" VNĐ"

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

        purchaseBtn.setOnClickListener { v: View? ->
            val purchaseScreen = PurchaseScreen(this, currentProduct!!)
            purchaseScreen.show(supportFragmentManager, "purchase_screen") //hiện fragment
            //static fragment
        }

        favoriteBtn.setOnClickListener { v: View? ->
            //thêm vào favorite
            val favorite= AppController.Favorite(currentProduct!!)
            addToFavorite(AppController.favorites,favorite)

            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this, "Đã thêm vào danh sách yêu thích", duration) // in Activity
            toast.show()
        }
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