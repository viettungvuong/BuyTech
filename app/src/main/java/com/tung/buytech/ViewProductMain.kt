package com.tung.buytech

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tung.buytech.AppController.Companion.addToFavorite
import com.tung.buytech.AppController.Companion.favorites
import com.tung.buytech.AppController.Companion.findProductImage
import com.tung.buytech.AppController.Companion.getDatabaseInstance
import com.tung.buytech.AppController.Companion.isAlreadyFavorite
import com.tung.buytech.AppController.Companion.removeFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewProductMain : AppCompatActivity() {
    companion object{
        @JvmStatic
        lateinit var currentProduct: AppController.Product
    }
    lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)

        val intent = getIntent()
        val name = intent.getStringExtra("ProductName")!!
        val price = intent.getLongExtra("ProductPrice",0)
        val productId = intent.getStringExtra("ProductId")!!
        val productImage = intent.getStringExtra("ProductImage")!!
        Log.d("ImageUrl",productImage)

        //tạo product tương ứng
        currentProduct=AppController.Product(name,price,productImage,productId)

        val productName: TextView
        val productPrice: TextView
        val productDescription: TextView

        productName = findViewById(R.id.product_title)
        productPrice = findViewById(R.id.product_price)
        productDescription = findViewById(R.id.product_description)
        productName.text = name
        productPrice.text = AppController.reformatNumber(price)+" VNĐ"

        //đặt hình ảnh sản phẩm
        val imgView = findViewById<ImageView>(R.id.product_image)
        findProductImage(productImage, imgView, this)

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
        val favoriteBtn = findViewById<ImageButton>(R.id.buttonFavorite)

        //kiểm tra sản phẩm có trong favorite
        var isAlreadyFavorite = isAlreadyFavorite(currentProduct!!)
        Log.d("isALREADYfavorite",isAlreadyFavorite.toString())
        if (isAlreadyFavorite){
            favoriteBtn.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.already_favorite))
        }
        //chỉnh button báo hiệu sản phẩm này đã có trong Favorite

        purchaseBtn.setOnClickListener { v: View? ->
            val purchaseScreen = PurchaseScreen(this, currentProduct!!)
            purchaseScreen.show(supportFragmentManager, "purchase_screen") //hiện fragment
            //static fragment
        }

        favoriteBtn.setOnClickListener { v: View? ->
            //thêm vào favorite
            if (!isAlreadyFavorite){
                val favorite= AppController.Favorite(currentProduct!!)
                addToFavorite(favorites,favorite)

                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this, "Đã thêm vào danh sách yêu thích", duration) // in Activity
                toast.show()

                favoriteBtn.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.already_favorite))

            } //bỏ khỏi favorite
            else{
                removeFavorite(AppController.Favorite(currentProduct))
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this, "Đã xoá khỏi danh sách yêu thích", duration) // in Activity
                toast.show()

                favoriteBtn.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.favorite_btn))
            }
            isAlreadyFavorite=!isAlreadyFavorite
        }

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler = BottomNavigationHandler(this,bottomNavBar)
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
                }
            } else {
            }
        }
    }


}