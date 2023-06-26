package com.tung.buytech

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController

class UserPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_page);

        val signOutBtn = findViewById<Button>(R.id.signOutBtn)
        val changePasswordBtn = findViewById<Button>(R.id.changePasswordBtn)

        val userText = findViewById<TextView>(R.id.userText)

        userText.setText(AppController.user.uid?:"Chưa đăng nhập")

        signOutBtn.setOnClickListener(
            View.OnClickListener {
                AccountFunctions.signOut(this)
            }
        )

        changePasswordBtn.setOnClickListener(
            View.OnClickListener {

            }
        )
    }


}