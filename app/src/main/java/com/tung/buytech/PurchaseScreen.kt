package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Task
import com.tung.buytech.AppController.Companion.getDatabaseInstance

class PurchaseScreen
@JvmOverloads constructor(
    context: Context, product: AppController.Product
) :AppCompatActivity() {
    companion object
    {
        @JvmStatic
        lateinit var location: String
        lateinit var seller: String
        lateinit var phoneNumber: String
    }
    lateinit var currentProduct: AppController.Product
    init { //những cái trong constructor dùng được từ init
        val currentProduct = product
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_screen)

        loadInformation(currentProduct)
    }

    fun loadInformation(product: AppController.Product){
        val docRef = getDatabaseInstance().collection("Products").document(product.productId).get()

        docRef.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    seller = document["seller"].toString() //set text
                    phoneNumber = document["phoneNumber"].toString() //set text
                    location = document["location"].toString() //set text
                } else {
                    Log.d("Docnodoc", "No such document")
                }
            } else {
                Log.d("Docfailed", "get failed with ", task.exception)
            }
        }
    }

    //gọi cho người bán
    fun call(phoneNumber: String){
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    //định vị
    fun navigate(address: String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(reformatLocation(address))
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    //tính năng chat
    fun chat(seller: String){

    }

    fun reformatLocation(address: String): String{
        var res=" ";
        val words = address.split(' ') //tách từ
        for (word in words){
            res= "$res$word+" //thêm dấu +
        }
        return res
    }
}

