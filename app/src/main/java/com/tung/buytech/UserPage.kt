package com.tung.buytech

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_page);

        val signOutBtn = findViewById<Button>(R.id.signOutBtn)
        val changePasswordBtn = findViewById<Button>(R.id.changePasswordBtn)

        val userText = findViewById<TextView>(R.id.userText)

        var userName = "" //hiện cái gì ở màn hình (phần tên)

        val user = Firebase.auth.currentUser
        if (user==null){
            userName="Chưa đăng nhập"
        }
        else{
            userName=user!!.email.toString()
        }

        userText.setText(userName)

        signOutBtn.setOnClickListener(
            View.OnClickListener {
                AccountFunctions.signOut(this)
            }
        )

        changePasswordBtn.setOnClickListener(
            View.OnClickListener {
                if (Firebase.auth.currentUser!=null) //phải đăng nhập tài khoản rồi mới được
                      confirmAccountDialog() //xác thực trước
                else{
                    Toast.makeText(
                        this,
                        "Chưa đăng nhập nên không thể đổi mật khẩu",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        )

        var navBar=findViewById<BottomNavigationView>(R.id.navigation)
        navBar.selectedItemId=R.id.account
        navBar.setOnItemSelectedListener { item ->
            // do stuff
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.cart -> {
                    val intent = Intent(this, Cart::class.java)
                    startActivity(intent)
                }
                R.id.favorite -> {
                    val intent = Intent(this, Favorites::class.java)
                    startActivity(intent)
                }
                R.id.sell -> {
                    val intent = Intent(this, SellPage::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    val intent = Intent(this, UserPage::class.java)
                    startActivity(intent)
                }
            }

            return@setOnItemSelectedListener true
        }

    }

    //show một dialog để đổi password
    private fun changePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.change_password, null)


        val newPasswordEditText = dialogView.findViewById<TextInputEditText>(R.id.newPassword)

        //để khi gõ xong là tắt bàn phím
        newPasswordEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // do something, e.g. set your TextView here via .setText()
                val imm: InputMethodManager =
                    v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Thay đổi mật khẩu")
            .setPositiveButton("Thay đổi") { dialogInterface: DialogInterface, _: Int ->

                AccountFunctions.changePassword(this, newPasswordEditText.text.toString())
                dialogInterface.dismiss()
            }
            .setNegativeButton("Huỷ") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun confirmAccountDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_user_confirm, null)

        val accountName = dialogView.findViewById<TextInputEditText>(R.id.username)
        val confirmPasswordEditText = dialogView.findViewById<TextInputEditText>(R.id.password)

        val user = Firebase.auth.currentUser
        accountName.setText(user!!.email)

        confirmPasswordEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // do something, e.g. set your TextView here via .setText()
                val imm: InputMethodManager =
                    v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Xác nhận tác khoản")
            .setPositiveButton("Xác nhận") { dialogInterface: DialogInterface, _: Int ->

                //bước reauthenticate (xác nhận tài khoản hiện tại)
                val credential = EmailAuthProvider
                    .getCredential(user!!.email.toString(), confirmPasswordEditText.text.toString())

                var reauthenticateSuccess=false

                user!!.reauthenticate(credential)
                    .addOnCompleteListener {
                        reauthenticateSuccess=true
                    }

                if (!reauthenticateSuccess){
                    Toast.makeText(
                        this,
                        "Không thể xác minh tài khoản",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                else{
                    changePasswordDialog()
                    //cho phép đổi mật khẩu sau khi xác minh xong
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Huỷ") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }


}