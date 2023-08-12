package com.tung.buytech.control

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.tung.buytech.objects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.coroutines.suspendCoroutine

const val homeFragmentTag="Home"
const val cartFragmentTag="Cart"
const val favoriteFragmentTag="Favorite"
const val sellFragmentTag="Sell"
const val accountFragmentTag="Account"

const val collectionProducts = "Items"
const val collectionUsers= "Users"

const val fieldProduct = "name"
const val fieldPrice = "price"
const val fieldImage = "image"
const val fieldProductId = "productId"


class AppController {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmField
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")

        @JvmStatic
        fun findProductImage(imageUrl: String, imageView: ImageView, context: Context) {
            //lấy link ảnh trên storage
            var imageFromStorage = ""
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                try {
                    imageFromStorage = getDownloadUrl(imageUrl)
                    //Log.d("ImageUrlSuccess", imageFromStorage)
                    Glide.with(context)
                        .load(imageFromStorage)
                        .into(imageView)
                } catch (exception: Exception) {
                    // Handle the exception if download URL retrieval fails
                    println("Error retrieving download URL: ${exception.message}")
                }
            }

        }

        @JvmField
        val productList= LinkedList<Product>()
        var db = Firebase.firestore
        val storageRef = Firebase.storage.reference

        lateinit var autoComplete: AutoComplete

        var favorites = LinkedList<Favorite>()

        var cart = Cart() //giỏ hàng

        var updateThreads = Executors.newSingleThreadScheduledExecutor()
        //thread pool

        @JvmStatic
        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        @JvmStatic
        fun getDatabaseInstance(): FirebaseFirestore {
            return db
        }

        @JvmStatic
        //reformat định dạng số
        fun reformatNumber(money: Long): String {
            if (money <= 100)
                return money.toString()


            var moneyString = money.toString();

            val strings = ArrayList<String>()

            val n = moneyString.length - 1;

            for (i in n downTo 0 step 3) {
                val start = Integer.max(i - 2, 0)
                val end = Integer.min(n + 1, i + 1)
                val s = moneyString.substring(start, end)
                strings.add(s)
                strings.add(",")
            }

            if (strings[strings.size - 1] == ",") {
                strings.removeAt(strings.size - 1);
            }

            strings.reverse() //đảo ngược mảng

            moneyString = ""

            for (i in 0..strings.size - 1) {
                moneyString += strings[i]
            }

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

        //xoá khỏi favorite
        fun removeFavorite(position: Int){
            val removed = favorites.get(position)
            val productId = removed.productId

            favorites.removeAt(position)

            //xoá trên firebase
            db.collection("users")
                .document(Firebase.auth.currentUser!!.uid).collection("favorites").document(productId).delete()
        }

        fun removeFavorite(favorite: Favorite){
            //linear search sẽ tốt hơn
            var i=0
            while (i< favorites.size){
                if (favorites[i].productId==favorite.productId){
                    break
                }
                i++
            }
            favorites.removeAt(i)

            //xoá trên firebase
           db.collection("users")
                .document(Firebase.auth.currentUser!!.uid).collection("favorites").document(favorite.productId).delete()
        }

        //tải danh sách favorite xuống
        fun fetchFavorites() {

            //lấy từ collection Favorites
            val getFavorites = db.collection("users")
                .document(Firebase.auth.currentUser!!.uid).collection("favorites")

            getFavorites.get()
                .addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot){
                        val productId = document.id

                        bindProductById(productId){
                            product ->  favorites.add(Favorite(product))
                        }
                    }
                }
        }

        //thêm vào favorites
        @JvmStatic
        fun addToFavorite(favorites: LinkedList<Favorite>, favorite: Favorite) {
            favorites.add(favorite)
            //thêm vào danh sách favorites local

            val productDoc = hashMapOf(
                fieldProductId to favorite.productId
            )

            //add vào collection trên firebase
             db.collection("users")
                .document(Firebase.auth.currentUser!!.uid).collection("favorites").document(favorite.productId).set(productDoc)
        }

        @JvmStatic
        fun bindProductById(productId: String, callback: (Product) -> Unit){
            db.collection("Items").document(productId).get().addOnSuccessListener {
                    document->
                val productName = document.getString(fieldProduct)
                val productPrice = document.getLong(fieldPrice)
                val productImageUrl = (document.get(fieldImage) as ArrayList<String>).first()

                callback(Product(productName!!,productPrice!!,productImageUrl,productId))
            }
        }

        @JvmStatic
        fun bindProductPerson(documentName: String, callback: (PersonProduct) -> Unit){
            val list=documentName.split("-")
            val productId=list[1]
            val PersonId=list[0]
            bindProductById(productId) { bindedProduct ->
                callback(PersonProduct(Person(PersonId), bindedProduct)) //trả về PersonProduct
            }
        }

        //kiểm tra một product nào đó có là favorite hay chưa
        @JvmStatic
        fun isAlreadyFavorite(currentProduct: Product): Boolean{
            val currentProductId = currentProduct.productId
            val temp = favorites.toList().sortedBy { it.productId }

            var i=0
            while (i<temp.size&&temp[i].productId<=currentProductId){
                if (temp[i].productId==currentProductId){
                    return true
                }

                i++
            }

            return false
        }

    }



    fun buy() {
        //hiện purchase screen
    }

}