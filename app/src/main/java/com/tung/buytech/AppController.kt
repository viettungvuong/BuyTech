package com.tung.buytech

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.collections.ArrayList
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

class AppController {
    companion object {
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
        val productList: LinkedList<Product> = LinkedList()
        var db = Firebase.firestore
        val storageRef = Firebase.storage.reference


        @JvmStatic
        lateinit var autoComplete: AutoComplete

        @JvmStatic
        var favorites = LinkedList<Favorite>()

        @JvmStatic
        var cart = LinkedList<Product>() //giỏ hàng

        @JvmStatic
        var updateThreads = Executors.newSingleThreadScheduledExecutor()
        //thread pool

        @JvmStatic
        fun getDatabaseInstance(): FirebaseFirestore {
            return this.db
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
            favorites.removeAt(position)

            //xoá trên firebase
            val docRef = db.collection("favorites").document(Firebase.auth.currentUser!!.uid)

            val deleteProduct = hashMapOf<String, Any>(
                "products" to FieldValue.arrayRemove(removed.productId)
            )

            docRef.update(deleteProduct)
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
            val docRef = db.collection("favorites").document(Firebase.auth.currentUser!!.uid)

            val deleteProduct = hashMapOf<String, Any>(
                "products" to FieldValue.arrayRemove(favorite.productId)
            )

            docRef.update(deleteProduct)
        }

        //cập nhật danh sách favorite
        fun updateFavorite(){

            //lấy từ collection Favorites
            val getFavorites = db.collection("favorites")
                .document(Firebase.auth.currentUser!!.uid)

            getFavorites.get()
                .addOnSuccessListener {
                    documentSnapshot->
                    val productList = documentSnapshot.get("products") as ArrayList<String>
                    print(productList)
                    for (favoriteProductId in productList){
                        print(favoriteProductId)
                        //với mỗi productId, ta sẽ bind
                        //chữ join là đổi từ CompletableFuture<Product> thành Product

                        //Lấy collection Items
                        val getProduct = db.collection("Items")
                            .document(favoriteProductId as String)
                        getProduct.get().addOnSuccessListener {
                            documentSnapshot2->
                            val productName=documentSnapshot2.getString(fieldProduct)
                            val productPrice=documentSnapshot2.getLong(fieldPrice)
                            val productImage=(documentSnapshot2.get(fieldImage) as ArrayList<String>).first()
                            favorites.add(Favorite(productName!!,productPrice!!,productImage,favoriteProductId))
                        }

                    }
            }
        }

        //thêm vào favorites
        @JvmStatic
        fun addToFavorite(favorites: LinkedList<Favorite>, favorite: Favorite) {
            favorites.add(favorite)
            //thêm vào danh sách favorites

            //tạo list từ product id của favorite
            //cái này chỉ để khi mà chưa document hoặc chưa có field products trong document
            val data = arrayListOf(
                favorite.productId,
            )
            val createField = hashMapOf(
                "products" to data //tạo field cho products
            )

            //lấy collection favorite từ database
            //để add vào sau này
            val getFavorites =  db.collection("favorites")
                .document(Firebase.auth.currentUser!!.uid)

           getFavorites //lấy document trên firebase
                .get()
                .addOnCompleteListener(OnCompleteListener {
                    task->if (task.isSuccessful()) {
                        val document=task.result
                        if (document!=null){ //có document
                            if (document.exists()){
                                //nếu có field products rồi
                                if (document.contains("products")){
                                    //nếu có field Products
                                    getFavorites.update("products", FieldValue.arrayUnion(favorite.productId))
                                }
                                else{
                                    getFavorites.set(createField)
                                    //nếu không có field products
                                }
                            }
                            else{
                                getFavorites.set(createField)
                                //nếu không có document
                            }
                        }
                    }
                })
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
        fun bindProductPeople(documentName: String, callback: (PeopleProduct) -> Unit){
            val list=documentName.split("-")
            val productId=list[1]
            val peopleId=list[0]
            bindProductById(productId,{
                bindedProduct->
                callback(PeopleProduct(People(peopleId),bindedProduct)) //trả về peopleProduct
            })
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



    class People(userId: String) {
        lateinit var userId: String
        lateinit var name: String
        init {
            this.userId=userId
            //lấy user từ database
            db.collection("users").document(userId).get().addOnSuccessListener {
                document->
                this.name=document.getString("name").toString()
            }
        }
    }

    class PeopleProduct(people: People, product: Product){
        lateinit var people: People
        lateinit var product: Product
        init {
            this.people=people
            this.product=product
        }
    }

    class Message(content: String, a: People, b: People) {

    }



}