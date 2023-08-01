package com.tung.buytech

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.tung.buytech.AppController.Companion.fetchFavorites

const val homeFragmentTag="Home"
const val cartFragmentTag="Cart"
const val favoriteFragmentTag="Favorite"
const val sellFragmentTag="Sell"
const val accountFragmentTag="Account"

const val collectionProducts = "Items"
const val fieldProduct = "name"
const val fieldPrice = "price"
const val fieldImage = "image"

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationHandler: BottomNavigationHandler

    private fun setBottomNavChecked(){
        //xử lý trên bottom navigation view hiển thị là đang chọn cái gì
        if (supportFragmentManager.backStackEntryCount > 0) {

            val fragment =
                supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
            when (fragment) {
                homeFragmentTag -> findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(
                    R.id.home
                ).setChecked(true)
                cartFragmentTag -> findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(
                    R.id.cart
                ).setChecked(true)
                favoriteFragmentTag -> findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(
                    R.id.favorite
                ).setChecked(true)
                sellFragmentTag -> findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(
                    R.id.sell
                ).setChecked(true)
                accountFragmentTag -> findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(
                    R.id.account
                ).setChecked(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setBottomNavChecked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchFavorites() //cập nhật danh sách favorite

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler =
            BottomNavigationHandler(this, bottomNavigationView) //handler bottom navigation view

        supportFragmentManager.beginTransaction().replace(R.id.fragment,HomeFragment()).addToBackStack(
            homeFragmentTag).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate() //quay về fragment trước trong main activity
            //thêm chữ immediate là để cập nhật luôn
            setBottomNavChecked()
        } else {
            finishAffinity()
            System.exit(0) //thoát khỏi app luôn
        }

    }

}



class BottomNavigationHandler(activity: Activity, navBar: BottomNavigationView) {
    //dùng class này để quản lý bottom nav bar gọn hơn
    init {
        var currentSelected = 0
        navBar.selectedItemId = currentSelected //đặt index cho bottom nav bar

        navBar.setOnItemSelectedListener { item ->
            //khi bấm vào sẽ mở fragment tương ứng
            lateinit var selectedFragment: Fragment

            var tag = ""

            when (item.itemId) {
                R.id.home -> {
                    selectedFragment = HomeFragment()
                    tag = homeFragmentTag
                }
                R.id.cart -> {
                    selectedFragment = CartPage()
                    tag = cartFragmentTag
                }
                R.id.favorite -> {
                    selectedFragment = Favorites()
                    tag = favoriteFragmentTag
                }
                R.id.sell ->{
                    selectedFragment=SellPage()
                    tag = sellFragmentTag
                }
                R.id.account -> {
                    selectedFragment=UserPage()
                    tag = accountFragmentTag
                }
            }

            if (selectedFragment != null) {
                (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, selectedFragment)
                    .addToBackStack(tag).commit() //hiện fragment lên
            }

            return@setOnItemSelectedListener true
        }
    }
}