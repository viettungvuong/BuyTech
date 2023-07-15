package com.tung.buytech

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Favorites: AppCompatActivity() {
    lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_page)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        val recyclerView=findViewById<RecyclerView>(R.id.favRecyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter=CartRecyclerAdapter(this,AppController.favorites)
        recyclerView.adapter=adapter

        var navBar=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler=BottomNavigationHandler(this,navBar)

    }


}