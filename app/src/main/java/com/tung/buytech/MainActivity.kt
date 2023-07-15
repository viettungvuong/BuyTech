package com.tung.buytech

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.*
import com.google.firebase.storage.FirebaseStorage
import com.google.rpc.Help.Link
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.AppController.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    companion object {
        @JvmField
        var collectionProducts = "Items"
        var fieldProduct = "name"
        var fieldPrice = "price"
        var fieldImage = "image"
    }


    public fun getCollectionName(): String {
        return collectionProducts
    }

    lateinit var bottomNavigationHandler: BottomNavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        var searchBtn = findViewById<Button>(R.id.search)
        var searchBar = findViewById<TextInputEditText>(R.id.productSearch)


        searchBtn.setOnClickListener(
            View.OnClickListener {
                var toSearch = searchBar.text.toString() //lay string tu searchbar
                search(toSearch, db)
                searchBtn.hideKeyboard()
            }
        )

        var navBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler=BottomNavigationHandler(this,navBar)
    }

    fun search(productName: String, db: FirebaseFirestore) {
        var grid =  findViewById<GridLayout>(R.id.suggestedProducts)

        //search và thêm vào grid
        suggestions(productName, db, grid)
    }



    //hiện kết quả tìm kiếm
    fun suggestions(productName: String, db: FirebaseFirestore, grid: GridLayout) {
        //chiến thuật là ta sẽ gom lại những cái sản phẩm có tên đó
        //ta sẽ cho biết giá trung bình, giá cao nhất và giá rẻ nhất
        grid.removeAllViews() //xoá hết mọi view

        db.collection(collectionProducts)
            .whereEqualTo(fieldProduct, productName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Process the query results
                for (document in querySnapshot) {
                    // Access other fields as needed
                    grid.addView(productView(document)) //them product view vao grid layout
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                println("Error getting documents: $exception")
            }
    }

    //productview là hiện tóm tắt thông tin sản phẩm sau khi tìm kiếm
    fun productView(document: QueryDocumentSnapshot): ProductView {
        //đặt các thông tin cho productview

        Log.d("ID:", document.id)
        val id = document.id //lấy tên của document
        val name = document.getString(fieldProduct).toString() //dat label cho productview
        var price = document.getLong(fieldPrice)
        if (price == null) {
            price = 0
        }

        //lấy tên file ảnh
        var imageUrl = ""
        imageUrl = (document.get(fieldImage) as ArrayList<String>).first()
        //lấy phần tử đầu tiên của array field "Image"


        var product = AppController.Product(name, price, imageUrl, id)
        val res = ProductView(this, product)
        return res
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}