package com.tung.buytech

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationHandler(activity: Activity, navBar: BottomNavigationView) {
    //dùng class này để quản lý bottom nav bar gọn hơn
    init {
        var currentSelected=0

        when (activity) {
            is MainActivity -> {
                currentSelected = R.id.home
            }
            is Cart -> {
                currentSelected = R.id.cart
            }
            is UserPage -> {
                currentSelected = R.id.account
            }
            is SellPage -> {
                currentSelected = R.id.sell
            }
            is Favorites -> {
                currentSelected = R.id.favorite
            }
        }

        navBar.selectedItemId=currentSelected //đặt index cho bottom nav bar

        navBar.setOnItemSelectedListener { item ->
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