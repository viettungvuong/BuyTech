package com.tung.buytech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.AccountFunctions.Companion.auth

class UserLogin : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //đã đăng nhập rồi
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            //vào luôn main activity
        }
        AccountFunctions.auth= Firebase.auth //initialize authentication thư viện
    }

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

    fun loginOnClick(signIn: Boolean, userInput: TextInputEditText, passwordInput: TextInputEditText): OnClickListener{
        val user = userInput.text.toString()
        val password=passwordInput.text.toString()

        return View.OnClickListener {
            if (signIn){
                AccountFunctions.signIn(this, this, user, password)
            }
            else{
                AccountFunctions.signUp(this, this, user, password)
            }
        }
    }
}