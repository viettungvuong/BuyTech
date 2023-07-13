package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.LinkedList

class Cart: AppCompatActivity() {

    lateinit var bottomNavigationHandler: BottomNavigationHandler
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

        var navBar=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler=BottomNavigationHandler(this,navBar)

    }

    fun deleteFromCart(itemInCart: AppController.Product, cart: LinkedList<AppController.Product>){
        if (!cart.contains(itemInCart))
            return

        cart.removeAt(cart.indexOf(itemInCart))
    }

}