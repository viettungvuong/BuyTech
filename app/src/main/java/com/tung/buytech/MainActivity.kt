package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import com.google.android.material.textfield.TextInputEditText

import com.google.firebase.ktx.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var searchBtn=findViewById<Button>(R.id.search)
        var searchBar=findViewById<TextInputEditText>(R.id.textField)

        val db = Firebase.firestore

        searchBtn.setOnClickListener(
            View.OnClickListener {
                var toSearch=searchBar.text.toString() //lay string tu searchbar
                search(toSearch)
            }
        )
    }

    fun search(productName: String){
        var grid=suggestedProductsGrid()


    }

    fun suggestedProductsGrid(): GridLayout{
        return findViewById(R.id.suggestedProducts)
    }
}