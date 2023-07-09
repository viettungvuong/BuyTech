package com.tung.buytech

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Favorites: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_page)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        val recyclerView=findViewById<RecyclerView>(R.id.favRecyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter=CartRecyclerAdapter(this,AppController.favorites)
        recyclerView.adapter=adapter

        var navBar=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.selectedItemId=R.id.sell
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