package com.tung.buytech

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationHandler(activity: Activity, navBar: BottomNavigationView) {
    lateinit var navBar: BottomNavigationView
    //dùng class này để quản lý bottom nav bar gọn hơn
    init {
        this.navBar=navBar

        var currentSelected=0

        when (activity){
            MainActivity::class.java -> {
                currentSelected=R.id.home
            }
            Cart::class.java->{
                currentSelected=R.id.cart
            }
            UserPage::class.java->{
                currentSelected=R.id.account
            }
            SellPage::class.java->{
                currentSelected=R.id.sell
            }
            Favorites::class.java->{
                currentSelected=R.id.favorite
            }
        }

        this.navBar.selectedItemId=currentSelected //đặt index cho bottom nav bar

        this.navBar.setOnItemSelectedListener { item ->
            // do stuff
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                }
                R.id.cart -> {
                    val intent = Intent(activity, Cart::class.java)
                    activity.startActivity(intent)
                }
                R.id.favorite -> {
                    val intent = Intent(activity, Favorites::class.java)
                    activity.startActivity(intent)
                }
                R.id.sell -> {
                    val intent = Intent(activity, SellPage::class.java)
                    activity.startActivity(intent)
                }
                R.id.account -> {
                    val intent = Intent(activity, UserPage::class.java)
                    activity.startActivity(intent)
                }
            }

            return@setOnItemSelectedListener true
        }
    }
}