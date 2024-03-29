package com.tung.buytech.control

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.activities.MainActivity
import com.tung.buytech.control.AppController.Companion.db


class AccountFunctions {
    companion object{

        @JvmStatic
        fun signIn(activity: Activity, context: Context, username: String, password: String){
            Firebase.auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(
                            context,
                            "Đã đăng nhập thành công",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent =
                            Intent(context, MainActivity::class.java)
                        activity.startActivity(intent)

                    } else {
                        // If sign in fails, display a message t@o the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Không thể đăng nhập",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        @JvmStatic
        fun signUp(activity: Activity, context: Context, username: String, password: String){
            Firebase.auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(
                            context,
                            "Tạo tài khoản thành công",
                            Toast.LENGTH_SHORT,
                        ).show()

                        //update lên firebase
                        val userData = hashMapOf(
                            "name" to "A"
                        )
                        val accountFirebase = db.collection("users").document(Firebase.auth.currentUser!!.uid).set(userData)

                        val intent =
                            Intent(context, MainActivity::class.java)
                        activity.startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Không thể tạo tài khoản",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        @JvmStatic
        fun signOut(context: Context){
            Firebase.auth.signOut() //đăng xuất

            Toast.makeText(
                context,
                "Đã đăng xuất thành công",
                Toast.LENGTH_SHORT,
            ).show()


        }

        @JvmStatic
        fun changePassword(context: Context, newPassword: String){
            val user = Firebase.auth.currentUser

            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Đã đổi mật khẩu thành công",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }


    }
}