package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.tung.buytech.AppController.Companion.getDatabaseInstance

class PurchaseScreen
constructor(
    context: Context, product: AppController.Product
) : DialogFragment(R.layout.activity_purchase_screen) {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadInformation(currentProduct)

        val sellerText = view.findViewById<TextView>(R.id.seller)
        val locationText = view.findViewById<TextView>(R.id.location)
        val phoneNumberText = view.findViewById<TextView>(R.id.phoneNumber)

        sellerText.text= seller
        locationText.text= location
        phoneNumberText.text= phoneNumber

        val btnLocation = view.findViewById<Button>(R.id.navigation)
        val btnCall = view.findViewById<Button>(R.id.call)
        val btnMessage = view.findViewById<Button>(R.id.message)

        btnLocation.setOnClickListener(
            View.OnClickListener {
                navigate(location)
            }
        )

        btnCall.setOnClickListener(
            View.OnClickListener {
                call(phoneNumber)
            }
        )
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
            startActivity(intent)
    }

    //định vị
    fun navigate(address: String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(reformatLocation(address))
        }
            startActivity(intent)
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

