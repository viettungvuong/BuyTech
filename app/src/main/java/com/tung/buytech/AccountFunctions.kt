package com.tung.buytech

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AccountFunctions {
    companion object{
        lateinit var auth: FirebaseAuth

        @JvmStatic
        fun reauthenticate(username: String, password: String){
            val user = Firebase.auth.currentUser!!

            //hiện dialog

            val credential = EmailAuthProvider
                .getCredential(username, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { Log.d(ContentValues.TAG, "User re-authenticated.") }
        }

        @JvmStatic
        fun signIn(activity: Activity, context: Context, username: String, password: String){
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        @JvmStatic
        fun signUp(username: String, password: String){

        }

        @JvmStatic
        fun signOut(){
            Firebase.auth.signOut() //đăng xuất
        }

        @JvmStatic
        fun changePassword(newPassword: String){
            val user = Firebase.auth.currentUser

            reauthenticate(user!!.email.toString(), newPassword) //phải reauthenticate rồi mới đổi password được

            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "User password updated.")
                    }
                }
        }


    }
}