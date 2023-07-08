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

        //đặt recyclerView ở định dạng linearLayout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = layoutManager
        val adapter = CartRecyclerAdapter(this,AppController.cart)
        recyclerView.adapter = adapter
        //gán adapter vào recyclerview
    }
}