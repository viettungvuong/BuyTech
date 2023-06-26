package com.tung.buytech

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.tung.buytech.MainActivity.Companion.collectionProducts
import java.util.LinkedList
import java.util.concurrent.Executors
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

class AppController {
    companion object {
        @JvmField
        val productList: LinkedList<Product> = LinkedList()
        var userId: String = String() //chứa userID
        var db = Firebase.firestore
        val storageRef = Firebase.storage.reference

        var favorites: ArrayList<Favorite>  =ArrayList()

        var updateThreads =  Executors.newSingleThreadScheduledExecutor()
        //thread pool


        //danh sách favorites
        //dùng arrayList là vì có thể random access

        @JvmStatic fun getDatabaseInstance(): FirebaseFirestore {
            return this.db
        }

        @JvmStatic
        //reformat định dạng số
        public fun reformatNumber(money: Long): String {
            if (money <= 100)
                return money.toString()


            var moneyString = money.toString();

            val strings = ArrayList<String>()

            val n = moneyString.length - 1;

            for (i in n downTo 0 step 3) {
                val start = Integer.max(i - 2, 0)
                val end = min(n + 1, i + 1)
                val s = moneyString.substring(start, end)
                strings.add(s)
                Log.d("Number",strings[strings.size-1])
                strings.add(",")
                Log.d("Comma",strings[strings.size-1])
            }

            if (strings[strings.size - 1] == ",") {
                strings.removeAt(strings.size - 1);
            }

            strings.reverse() //đảo ngược mảng

            moneyString=""

            for (i in 0..strings.size-1){
                moneyString+=strings[i]
            }
            Log.d("MoneyString",moneyString)
            return moneyString;
            //gio ta phai cho no xuat dung chieu
        }

        @JvmStatic
        suspend fun getDownloadUrl(fileName: String): String {
            return suspendCoroutine { continuation ->
                val storageRef = FirebaseStorage.getInstance().reference
                val fileRef = storageRef.child(fileName)

                fileRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        continuation.resumeWith(Result.success(downloadUrl))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWith(Result.failure(exception))
                    }
            }
        }

        @JvmStatic
        fun addToFavorite(favorites: ArrayList<Favorite>, favorite: Favorite){
            favorites.add(favorite)
            //thêm vào danh sách favorites

            // Add a new document with a generated id.
            val data = hashMapOf(
                "id" to favorite.productId,
            )

            db.collection("Favorites")
                .document(userId) //chỗ này đặt tên cái userid
                .set(data)
                .addOnSuccessListener { documentReference ->
                }
                .addOnFailureListener { e ->
                }
        }

        @JvmStatic
        fun reauthenticate(){
            val user = Firebase.auth.currentUser!!

            var username=""
            var password=""

            //hiện dialog

            val credential = EmailAuthProvider
                .getCredential(username, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
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
                        Log.d(TAG, "User password updated.")
                    }
                }
        }

    }
    open class Product(name: String, price: Long, imageUrl: String, productId: String ){
        public var name: String = name
        public var price: Long = price
        public var imageUrl: String = imageUrl
        public var productId: String= productId
        //khúc này là constructor của class
        //ktra thong tin mat hang
        //observer design pattern
        fun updateStatus(){
            val docRef = db.collection(collectionProducts).document(productId)
            docRef.addSnapshotListener{
                    snapshot,e->
                if (snapshot!=null&&snapshot.exists()){
                    val inStock = Integer.parseInt(snapshot.getString("In stock"))
                    var sold= false
                    if (inStock>0){
                        sold=true //đã bán hết sản phẩm
                    }

                }
            }
        }
    }

    //inheritance
    class Favorite(name: String, price: Long, imageFile: String, productId: String): Product(name,price,imageFile,productId){
        constructor(product: Product) : this(product.name,product.price,product.imageUrl,product.productId) {
            //constructor thứ hai của class
        }

        fun notifyMe(){
            //thông báo khi hết hàng
            var notification = "Mặt hàng đã hết :("

        }
    }



    //thread java
    class UpdateThread: Runnable{


        public override fun run() {
           //threadpool

        }

    }

    fun buy(){

    }


}