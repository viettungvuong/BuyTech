package com.tung.buytech.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tung.buytech.control.AppController.Companion.getDatabaseInstance
import com.tung.buytech.R
import com.tung.buytech.objects.Product

class PurchaseScreen
constructor(
    context: Context, product: Product
) : DialogFragment(R.layout.activity_purchase_screen) {

    lateinit var currentProduct: Product
    lateinit var location: String
    lateinit var seller: String
    lateinit var phoneNumber: String

    init { //init chính là những hành động sẽ tiến hành trong giai đoạn construct
        currentProduct = product

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.activity_purchase_screen, null)
        //inflate file xml layout và đặt nó là view
        builder.setView(view)

        val sellerText = view.findViewById<TextView>(R.id.seller)
        val locationText = view.findViewById<TextView>(R.id.location)
        val phoneNumberText = view.findViewById<TextView>(R.id.phoneNumber)
        val btnLocation = view.findViewById<Button>(R.id.navigation)
        val btnCall = view.findViewById<Button>(R.id.call)
        val btnMessage = view.findViewById<Button>(R.id.message)

        loadInformation(currentProduct){
            sellerText.text = seller
            locationText.text = location
            phoneNumberText.text = phoneNumber
            btnLocation.setOnClickListener {
                navigate(location)
            }

            btnCall.setOnClickListener {
                call(phoneNumber)
            }
        } //khi hàm loadInformation xong nó sẽ tiến hành những hành động trong ngoặc này

        return builder.create()
    }

    fun loadInformation(product: Product, callback: () -> Unit){
        val docRef = getDatabaseInstance().collection("Products").document(product.productId).get()

        docRef.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    this.seller = document["seller"].toString() //set text
                    this.phoneNumber = document["phoneNumber"].toString() //set text
                    this.location = document["location"].toString() //set text

                    //do cái này là async function nên ta bỏ vào phần isSuccessful
                    //khi hàm loadInformation xong nó sẽ gọi callback
                    callback()
                }
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

