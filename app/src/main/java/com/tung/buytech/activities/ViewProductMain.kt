package com.tung.buytech.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tung.buytech.R
import com.tung.buytech.control.AppController
import com.tung.buytech.control.AppController.Companion.addToFavorite
import com.tung.buytech.control.AppController.Companion.favorites
import com.tung.buytech.control.AppController.Companion.findProductImage
import com.tung.buytech.control.AppController.Companion.getDatabaseInstance
import com.tung.buytech.control.AppController.Companion.isAlreadyFavorite
import com.tung.buytech.control.AppController.Companion.removeFavorite
import com.tung.buytech.control.collectionProducts
import com.tung.buytech.fragments.PurchaseScreen
import com.tung.buytech.objects.Favorite
import com.tung.buytech.objects.Product


class ViewProductMain : AppCompatActivity() {
    companion object{
        @JvmStatic
        lateinit var currentProduct: Product
    }
    lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)

        val intent = getIntent()
        currentProduct =intent.getSerializableExtra("Product") as Product


        val productName: TextView
        val productPrice: TextView
        val productDescription: TextView
        productName = findViewById(R.id.product_title)
        productPrice = findViewById(R.id.product_price)
        productDescription = findViewById(R.id.product_description)

        productName.text = currentProduct.name
        productPrice.text = AppController.reformatNumber(currentProduct.price)+" VNĐ"

        //đặt hình ảnh sản phẩm
        val imgView = findViewById<ImageView>(R.id.product_image)
        findProductImage(currentProduct.imageUrl, imgView, this)

        //mô tả sản phẩm
        getDescription(currentProduct.productId, productDescription) //lấy mô tả sản phẩm
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
        if (isAlreadyFavorite){
            favoriteBtn.setImageDrawable(AppCompatResources.getDrawable(this,
                R.drawable.already_favorite
            ))
        }
        //chỉnh button báo hiệu sản phẩm này đã có trong Favorite

        //hỏi mua
        purchaseBtn.setOnClickListener { v: View? ->
            val purchaseScreen = PurchaseScreen(this, currentProduct!!)
            supportFragmentManager.beginTransaction().replace(R.id.fragment,purchaseScreen).commit() //hiện fragment lên
        }

        favoriteBtn.setOnClickListener { v: View? ->
            //thêm vào favorite (do chưa có trong favorite)
            val duration = Toast.LENGTH_SHORT
            lateinit var toast: Toast

            if (!isAlreadyFavorite){
                val favorite= Favorite(currentProduct!!)
                addToFavorite(favorites,favorite)

                toast = Toast.makeText(this, "Đã thêm vào danh sách yêu thích", duration) // in Activity

                favoriteBtn.setImageDrawable(AppCompatResources.getDrawable(this,
                    R.drawable.already_favorite
                ))

            } //bỏ khỏi favorite (do đã có trong favorite)
            else{
                removeFavorite(Favorite(currentProduct))
                toast = Toast.makeText(this, "Đã xoá khỏi danh sách yêu thích", duration) // in Activity
                toast.show()

                favoriteBtn.setImageDrawable(AppCompatResources.getDrawable(this,
                    R.drawable.favorite_btn
                ))
            }
            toast.show()
            isAlreadyFavorite=!isAlreadyFavorite
        }

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler = BottomNavigationHandler(this,bottomNavBar)
    }

    fun getDescription(productId: String?, descriptionText: TextView) { //lấy description
        val docRef = getDatabaseInstance().collection(collectionProducts).document(
            productId!!
        )

        //lấy document
        val document = docRef.get() //Task là một dạng asynchronous (ví dụ như Runnable)
        document.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    descriptionText.text = document["description"].toString() //set text
                } else {
                    //không có document
                    val toast = Toast.makeText(this,"Không có sản phẩm",Toast.LENGTH_SHORT)
                    toast.show()
                    this.finish()
                }
            } else {
            }
        }
    }


}