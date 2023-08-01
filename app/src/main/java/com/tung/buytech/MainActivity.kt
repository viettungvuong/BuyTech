package com.tung.buytech

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.*
import com.google.firebase.storage.FirebaseStorage
import com.google.rpc.Help.Link
import com.tung.buytech.AppController.Companion.bindProductById
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.AppController.Companion.getDatabaseInstance
import com.tung.buytech.AppController.Companion.fetchFavorites
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

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

        FirebaseApp.initializeApp(this)
        fetchFavorites() //cập nhật danh sách favorite

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationHandler =
            BottomNavigationHandler(this, bottomNavigationView) //handler bottom navigation view
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
                    selectedFragment = Cart()
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