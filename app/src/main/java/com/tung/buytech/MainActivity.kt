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
import com.tung.buytech.AppController.Companion.updateFavorite
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

class MainActivity : AppCompatActivity() {
    companion object {
        @JvmField
        var collectionProducts = "Items"
        var fieldProduct = "name"
        var fieldPrice = "price"
        var fieldImage = "image"
    }

    lateinit var bottomNavigationHandler: BottomNavigationHandler

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        updateFavorite() //cập nhật danh sách favorite
    }

}

const val homeFragmentTag="Home"
const val cartFragmentTag="Cart"
const val favoriteFragmentTag="Favorite"
const val sellFragmentTag="Sell"
const val accountFragmentTag="Account"

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