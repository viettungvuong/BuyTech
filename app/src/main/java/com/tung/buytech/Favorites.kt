package com.tung.buytech

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Favorites: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_page)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        val recyclerView=findViewById<RecyclerView>(R.id.favRecyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter=CartRecyclerAdapter(AppController.favorites)
        recyclerView.adapter=adapter
    }
}