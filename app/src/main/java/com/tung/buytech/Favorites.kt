package com.tung.buytech

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tung.buytech.AppController.Companion.favorites

class Favorites: AppCompatActivity() {
    lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_page)


        var navBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler = BottomNavigationHandler(this, navBar)

        if (favorites.isEmpty()){
            //nếu favorite empty thì mới hiện
            supportFragmentManager.beginTransaction()
                .add(R.id.container,EmptyFavoriteFragment::class.java,null)
                .commit() //thêm vào supportFragmentManager
        } else {
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
            val recyclerView = findViewById<RecyclerView>(R.id.favRecyclerView)
            recyclerView.layoutManager = layoutManager
            val adapter = CartRecyclerAdapter(this, AppController.favorites)
            recyclerView.adapter = adapter

            val swipeHelperCallback =
                SwipeRecyclerHelper(adapter, this) //cái này là callback của itemTouchHelper
            val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
            itemTouchHelper.attachToRecyclerView((recyclerView))

        }

    }


}