package com.tung.buytech

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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        updateFavorite() //cập nhật danh sách favorite
    }

}

class BottomNavigationHandler(activity: Activity, navBar: BottomNavigationView) {
    //dùng class này để quản lý bottom nav bar gọn hơn
    init {
        var currentSelected=0
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