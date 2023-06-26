package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class UserLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val userInput = findViewById<TextInputEditText>(R.id.username)
        val passwordInput = findViewById<TextInputEditText>(R.id.password)

        val loginBtn = findViewById<Button>(R.id.signInBtn)

        userInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used in this example
            }

            override fun afterTextChanged(s: Editable?) {
                val input=s.toString()

                if (input.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    // không phải định dạng email
                    return
                }

                // Check if an account with the corresponding email exists on Firebase Authentication
                val auth = FirebaseAuth.getInstance()
                auth.fetchSignInMethodsForEmail(input)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods //nếu null thì là không có tài khoản
                            if (signInMethods.isNullOrEmpty()) {
                                //không có tài khoản
                                loginBtn.setText("Đăng kí")
                            } else {
                                loginBtn.setText("Đăng nhập")
                                //có tài khoản
                            }
                        }/* else {
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidCredentialsException) {
                                // Invalid email format
                                // Handle accordingly
                            } else {
                                // Error occurred while checking account existence
                                // Handle accordingly
                            }
                        }*/
                    }
            }
        })


    }
}