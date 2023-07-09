package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomBarFragment
constructor(
    context: Context
): NavHostFragment() {

    init {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.bar_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            // do stuff
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.cart -> {
                    val intent = Intent(context, Cart::class.java)
                    startActivity(intent)
                }
                R.id.favorite -> {
                    val intent = Intent(context, Favorites::class.java)
                    startActivity(intent)
                }
                R.id.sell -> {
                    val intent = Intent(context, SellPage::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    val intent = Intent(context, UserPage::class.java)
                    startActivity(intent)
                }
            }

            return@setOnItemSelectedListener true
        }
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}