package com.tung.buytech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Cart: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart)

        val recyclerView = findViewById<RecyclerView>(R.id.cartView)

        val layoutManager = LinearLayoutManager(this) // or GridLayoutManager, StaggeredGridLayoutManager, etc.
        recyclerView.layoutManager = layoutManager
        val adapter = CartRecyclerAdapter(AppController.productList)
        recyclerView.adapter = adapter
    }
}