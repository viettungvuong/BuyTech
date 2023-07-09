package com.tung.buytech

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Cart: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart)

        val recyclerView = findViewById<RecyclerView>(R.id.cartView)

        //đặt recyclerView ở định dạng linearLayout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = layoutManager
        val adapter = CartRecyclerAdapter(this,AppController.cart)
        recyclerView.adapter = adapter
        //gán adapter vào recyclerview

        var navBar=findViewById<BottomNavigationView>(R.id.navigation)
        navBar.selectedItemId=R.id.cart
        navBar.setOnItemSelectedListener { item ->
            // do stuff
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.cart -> {
                    val intent = Intent(this, Cart::class.java)
                    startActivity(intent)
                }
                R.id.favorite -> {
                    val intent = Intent(this, Favorites::class.java)
                    startActivity(intent)
                }
                R.id.sell -> {
                    val intent = Intent(this, SellPage::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    val intent = Intent(this, UserPage::class.java)
                    startActivity(intent)
                }
            }

            return@setOnItemSelectedListener true
        }

    }
}