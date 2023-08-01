package com.tung.buytech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.AppController.Companion.storageRef
import java.util.*


class SellPage : Fragment() {

    private lateinit var bottomNavigationHandler: BottomNavigationHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.sell_product, container, false)

        val priceInput = rootView.findViewById<TextInputEditText>(R.id.productPrice)
        priceInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Text formatting logic...
            }
        })

        val gallery = rootView.findViewById<LinearLayout>(R.id.images)
        // Rest of the code related to gallery and media selection...

        val addImageBtn = rootView.findViewById<Button>(R.id.addImageBtn)
        addImageBtn.setOnClickListener {
            // Add image button click logic...
        }

        val sellBtn = rootView.findViewById<Button>(R.id.sellBtn)
        sellBtn.setOnClickListener {
            // Sell button click logic...
        }

        return rootView
    }

    // Other functions can be placed here...

}
