package com.tung.buytech.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.control.AccountFunctions
import com.tung.buytech.R

class UserPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.user_page, container, false)

        val signOutBtn = rootView.findViewById<Button>(R.id.signOutBtn)
        val changePasswordBtn = rootView.findViewById<Button>(R.id.changePasswordBtn)

        val userText = rootView.findViewById<TextView>(R.id.userText)

        var userName = "" // Display name on the screen (e.g., email)

        val user = Firebase.auth.currentUser
        if (user == null) {
            userName = "Chưa đăng nhập"
        } else {
            userName = user!!.email.toString()
        }

        userText.text = userName

        signOutBtn.setOnClickListener {
            AccountFunctions.signOut(requireContext())
        }

        changePasswordBtn.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                // User must be signed in before changing the password, so show confirmation dialog first.
                confirmAccountDialog()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Chưa đăng nhập nên không thể đổi mật khẩu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return rootView
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

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Thay đổi mật khẩu")
            .setPositiveButton("Thay đổi") { dialogInterface: DialogInterface, _: Int ->

                AccountFunctions.changePassword(
                    requireContext(),
                    newPasswordEditText.text.toString()
                )
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

        val dialogBuilder = AlertDialog.Builder(requireContext())
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
                        requireContext(),
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