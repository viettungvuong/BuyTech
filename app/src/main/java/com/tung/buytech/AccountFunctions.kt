package com.tung.buytech

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFunctions {
    companion object{
        @JvmStatic
        fun reauthenticate(){
            val user = Firebase.auth.currentUser!!

            var username=""
            var password=""

            //hiện dialog

            val credential = EmailAuthProvider
                .getCredential(username, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { Log.d(ContentValues.TAG, "User re-authenticated.") }
        }

        @JvmStatic
        fun signIn(username: String, password: String){

        }

        @JvmStatic
        fun signUp(username: String, password: String){

        }

        @JvmStatic
        fun signOut(){
            Firebase.auth.signOut() //đăng xuất
        }

        @JvmStatic
        fun changePassword(){
            reauthenticate() //phải reauthenticate rồi mới đổi password được

            val user = Firebase.auth.currentUser
            val newPassword = ""

            //hiện dialgo

            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "User password updated.")
                    }
                }
        }


    }
}