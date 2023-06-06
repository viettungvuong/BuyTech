package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var searchBtn=findViewById<Button>(R.id.search)
        var searchBar=findViewById<TextInputEditText>(R.id.textField)

        searchBtn.setOnClickListener(
            View.OnClickListener {
                var toSearch=searchBar.text.toString() //lay string tu searchbar
                search(toSearch)
            }
        )
    }

    fun search(productName: String){

    }
}