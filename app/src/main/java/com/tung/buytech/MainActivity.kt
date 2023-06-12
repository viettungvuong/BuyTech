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
import com.tung.buytech.AppController.Companion.db
import java.lang.Integer.max
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.util.LinkedList
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    final var collectionProducts = "Items"
    final var fieldProduct = "name"
    final var fieldPrice = "price"
    final var fieldImage = "image"
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

        var navBar=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.setOnItemSelectedListener { item ->
            // do stuff
            when (item.itemId) {
                R.id.home -> {
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.cart -> {
                    val intent= Intent(this,Cart::class.java)
                    startActivity(intent)
                }
                R.id.favorite -> {
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.sell->{
                    val intent= Intent(this,SellPage::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    val intent= Intent(this,UserPage::class.java)
                    startActivity(intent)
                }
            }

            return@setOnItemSelectedListener true
        }
    }

    fun search(productName: String, db: FirebaseFirestore) {
        var grid = suggestedProductsGrid()

        suggestions(productName, db, grid)
    }

    fun suggestions(productName: String, db: FirebaseFirestore, grid: GridLayout) {
        //chiến thuật là ta sẽ gom lại những cái sản phẩm có tên đó
        //ta sẽ cho biết giá trung bình, giá cao nhất và giá rẻ nhất
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

    fun suggestedProductsGrid(): GridLayout {
        return findViewById(R.id.suggestedProducts)
    }

    fun productView(document: QueryDocumentSnapshot): ProductView {
        val res = ProductView(this)
        res.setLabel(document.getString(fieldProduct).toString()) //dat label cho productview
        var price=document.getString(fieldPrice).toString()
        res.setPrice(reformatNumber(parseLong(price))+" VNĐ")

        var imageUrl=""
        imageUrl=(document.get(fieldImage) as ArrayList<String>).first()
        //lấy phần tử đầu tiên của array field "Image"

        getDownloadUrl(imageUrl,
            onSuccess = { s ->
                res.setProductImage(s) //nếu lấy thành công thì set hình ảnh
            },
            onFailure = { exception ->
                // Handle download URL retrieval failure
                println("Error retrieving download URL: $exception")
            })

        return res
    }

    //lấy url của ảnh lưu trong Firebase Storage
    fun getDownloadUrl(
        fileName: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child(fileName)

        fileRef.downloadUrl
            .addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                onSuccess(downloadUrl)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    public fun reformatNumber(money: Long): String {
        if (money <= 100)
            return money.toString()


        var moneyString = money.toString();

        val strings = ArrayList<String>()

        val n = moneyString.length - 1;

        for (i in n downTo 0 step 3) {
            val start = max(i - 2, 0)
            val end = min(n + 1, i + 1)
            val s = moneyString.substring(start, end)
            strings.add(s)
            Log.d("Number",strings[strings.size-1])
            strings.add(",")
            Log.d("Comma",strings[strings.size-1])
        }

        if (strings[strings.size - 1] == ",") {
            strings.removeAt(strings.size - 1);
        }

        strings.reverse() //đảo ngược mảng

        moneyString=""

        for (i in 0..strings.size-1){
            moneyString+=strings[i]
        }
        Log.d("MoneyString",moneyString)
        return moneyString;
        //gio ta phai cho no xuat dung chieu
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}