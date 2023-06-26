package com.tung.buytech

import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

        var userName = ""

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
                      showChangePasswordDialog()
                else{
                    Toast.makeText(
                        this,
                        "Chưa đăng nhập nên không thể đổi mật khẩu",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        )
    }

    //show một dialog để đổi password
    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.change_password, null)

        val newPasswordEditText = dialogView.findViewById<TextInputEditText>(R.id.newPassword)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Thay đổi mật khẩu")
            .setPositiveButton("Thay đổi") { dialogInterface: DialogInterface, _: Int ->

                //bước reauthenticate (xác nhận tài khoản hiện tại)
                val activityResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        //nếu kết quả trả về là oke
                        //ta tiến hành đổi password
                        AccountFunctions.changePassword(this, newPasswordEditText.text.toString())
                    }
                }

                val intent = Intent(this, UserConfirm::class.java)
                activityResultContract.launch(intent)

                // Do something with the entered text
                dialogInterface.dismiss()
            }
            .setNegativeButton("Huỷ") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

}