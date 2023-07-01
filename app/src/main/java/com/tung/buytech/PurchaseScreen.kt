package com.tung.buytech

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PurchaseScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_screen)

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