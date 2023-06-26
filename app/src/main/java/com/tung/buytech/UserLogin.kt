package com.tung.buytech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var loginInstance=true //kiểm tra sẽ đăng nhập hay đăng kí
        AccountFunctions.auth= Firebase.auth //initialize authentication thư viện

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
                                loginInstance=false
                            } else {
                                loginBtn.setText("Đăng nhập")
                                loginInstance=true
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

        passwordInput.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // do something, e.g. set your TextView here via .setText()
                val imm: InputMethodManager =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })

        loginBtn.setOnClickListener(loginOnClick(loginInstance,userInput, passwordInput))
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