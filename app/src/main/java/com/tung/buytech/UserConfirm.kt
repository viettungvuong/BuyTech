package com.tung.buytech

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserConfirm: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_confirm)


        val userInput = findViewById<TextInputEditText>(R.id.username)
        val passwordInput = findViewById<TextInputEditText>(R.id.password)

        val loginBtn = findViewById<Button>(R.id.signInBtn)

        userInput.setText(Firebase.auth.currentUser!!.email)

        loginBtn.setOnClickListener(
            View.OnClickListener {
                //hiện dialog
                val credential = EmailAuthProvider
                    .getCredential(Firebase.auth.currentUser!!.email.toString(), passwordInput.text.toString())

                Firebase.auth.currentUser!!.reauthenticate(credential)
                    .addOnCompleteListener {
                        val resultIntent = Intent()
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener{
                        Toast.makeText(
                            this,
                            "Không thể xác minh tài khoản",
                            Toast.LENGTH_SHORT,
                        ).show()
                        finish()
                    }


            }
        )
    }
}