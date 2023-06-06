package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.*

class MainActivity : AppCompatActivity() {
    final var collectionProducts="Items"
    final var fieldProduct="name"
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

    fun search(productName: String, db: FirebaseFirestore){
        var grid=suggestedProductsGrid()

        var docRef=db.collection("Items")
    }

    fun suggestions(productName: String, db: FirebaseFirestore){
        //chiến thuật là ta sẽ gom lại những cái sản phẩm có tên đó
        //ta sẽ cho biết giá trung bình, giá cao nhất và giá rẻ nhất
        db.collection(collectionProducts)
            .whereEqualTo(fieldProduct, productName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Process the query results
                for (document in querySnapshot) {
                    val documentId = document.id
                    val name = document.getString(fieldProduct)
                    // Access other fields as needed
                    println("Document ID: $documentId, Name: $name")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                println("Error getting documents: $exception")
            }
    }

    fun suggestedProductsGrid(): GridLayout{
        return findViewById(R.id.suggestedProducts)
    }
}