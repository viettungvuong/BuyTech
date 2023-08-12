package com.tung.buytech.activities

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.R
import com.tung.buytech.control.AccountFunctions

class UserLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        FirebaseApp.initializeApp(this)

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            Toast.makeText(
                this,
                "Đã đăng nhập thành công",
                Toast.LENGTH_SHORT,
            ).show()

            //đã đăng nhập rồi
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            //vào luôn main activity
        }

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
                var loginInstance=false //kiểm tra sẽ đăng nhập hay đăng kí
                // Check if an account with the corresponding email exists on Firebase Authentication
                val auth = FirebaseAuth.getInstance()
                auth.fetchSignInMethodsForEmail(input)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods //nếu null thì là không có tài khoản
                            if (signInMethods.isNullOrEmpty()) {
                                //không có tài khoản
                                loginInstance=false
                                loginBtn.setText("Đăng kí")

                            } else {
                                loginInstance=true
                                loginBtn.setText("Đăng nhập")

                                //có tài khoản
                            }
                        }
                        loginBtn.setOnClickListener(loginOnClick(loginInstance,userInput, passwordInput))
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


    }

    fun loginOnClick(signIn: Boolean, userInput: TextInputEditText, passwordInput: TextInputEditText): OnClickListener{
        return View.OnClickListener {
            val user = userInput.text.toString()
            val password=passwordInput.text.toString()
            //Log.d("Sign in", signIn.toString())

            if (signIn){
                AccountFunctions.signIn(this, this, user, password)
            }
            else{
                AccountFunctions.signUp(this, this, user, password)
            }
        }
    }
}