package com.tung.buytech

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.tung.buytech.MainActivity.Companion.collectionProducts
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

class AppController {
    companion object {
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
                Log.d("Number", strings[strings.size - 1])
                strings.add(",")
                Log.d("Comma", strings[strings.size - 1])
            }

            if (strings[strings.size - 1] == ",") {
                strings.removeAt(strings.size - 1);
            }

            strings.reverse() //đảo ngược mảng

            moneyString = ""

            for (i in 0..strings.size - 1) {
                moneyString += strings[i]
            }
            Log.d("MoneyString", moneyString)
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

        fun bindProduct(productId: String): Product{
            //lấy document product
            val getProduct = db.collection("products").document(productId)

            getProduct.get().addOnSuccessListener {
                documentSnapshot->
                val productname=documentSnapshot["name"].toString()
            }
        }

        //cập nhật danh sách favorite
        fun updateFavorite(){
            val getFavorites = db.collection("favorites")
                .document(Firebase.auth.currentUser!!.uid)

            getFavorites.get()
                .addOnSuccessListener {
                    documentSnapshot->
                    for (favoriteProductId in Arrays.asList(documentSnapshot["products"])){
                        //với mỗi productId, ta sẽ bind
                    }
            }
        }

        @JvmStatic
        fun addToFavorite(favorites: LinkedList<Favorite>, favorite: Favorite) {
            favorites.add(favorite)
            //thêm vào danh sách favorites

            //tạo list từ product id của favorite
            val data = arrayListOf(
                favorite.productId,
            )
            //tạo field cho products
            //data là arrayList
            val createField = hashMapOf(
                "products" to data
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
                        if (document!=null){
                            if (document.exists()){
                                if (document.contains("products")){
                                    //nếu có field Products
                                    getFavorites.update("products", FieldValue.arrayUnion(arrayOf(data)))
                                }
                                else{

                                    getFavorites.set(createField)
                                }
                            }
                            else{
                                getFavorites.set(createField)
                            }
                        }
                    }
                })
        }

    }

    open class Product(name: String, price: Long, imageUrl: String, productId: String) :
        java.io.Serializable {
        public var name: String = name
        public var price: Long = price
        public var imageUrl: String = imageUrl
        public var productId: String = productId
        //khúc này là constructor của class
        //ktra thong tin mat hang
        //observer design pattern

        public var sold = false //đã bán hay chưa

        fun updateSoldStatus(callback: () -> Unit) {
            val docRef = db.collection(collectionProducts).document(productId).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            val inStock = Integer.parseInt(document.getString("stock"))

                            if (inStock > 0) {
                                this.sold = true //đã bán hết sản phẩm
                            }
                        }
                    }

                }
        }
    }

    //inheritance
    class Favorite(name: String, price: Long, imageFile: String, productId: String) :
        Product(name, price, imageFile, productId) {
        constructor(product: Product) : this(
            product.name,
            product.price,
            product.imageUrl,
            product.productId
        ) {
            //constructor thứ hai của class
        }

        fun notifyMe() {
            //thông báo khi hết hàng
            updateSoldStatus() {
                if (sold) {
                    var notification = "Mặt hàng đã hết :("
                    //nếu đã hết hàng thì thông báo
                }

            } //dùng hàm callback để khi nào xong rồi mới báo

        }
    }


    //thread java
    //thread để kiểm tra tình trạng món hàng
    class UpdateThread : Runnable {
        public override fun run() {
            //threadpool

        }

    }

    fun buy() {
        //hiện purchase screen
    }


    class AutoComplete {
        lateinit var stackAutoComplete: Stack<ArrayList<String>>
        //stack là để phòng trường hợp người dùng xoá từ thì phải pop ra để lấy lại cái vừa trc đó

        init {
            lateinit var currentListOfTagsAndKeywords: ArrayList<String>

            //duyệt trong collection Products
            val collectionRef = getDatabaseInstance().collection("Products").get()

            collectionRef.addOnSuccessListener { result ->
                for (document in result) {
                    //thêm tag của một sản phẩm
                    val currentWords = document["tag"] as LinkedList<String>

                    for (word in currentWords) {
                        currentListOfTagsAndKeywords.add(word)
                    }
                }
            }

            //sort lại danh sách các từ khoá có thể ở hiện tại theo thứ tự chữ cái
            currentListOfTagsAndKeywords.sorted()

            stackAutoComplete.add(currentListOfTagsAndKeywords)
            //thêm list ban đầu vào stack
        } //cái init này hoạt động như là một constructor

        //khi nhập thêm một kí tự mới thì sẽ gọi hàm này
        fun autoCompleteAdd(currentlyTyping: String, pos: Int): ArrayList<String> {
            var currentList = stackAutoComplete.peek()
            //bài này ta dùng thuật toán 2 pointer vì đã sort list lại rồi
            //ta chỉ cần tìm phạm vi (từ đầu đến cuối) của các từ khoá có thể có của những kí tự đang nhập
            //ta sẽ lấy kí tự ở vị trí pos của left và right để so với kí tự hiện tại của currentlyTyping
            while (currentList.isNotEmpty() && currentList.first()[pos] != currentlyTyping.last()) {
                currentList.removeFirst()
            }

            while (currentList.isNotEmpty() && currentList.last()[pos] != currentlyTyping.last()) {
                currentList.removeLast();
            }

            stackAutoComplete.push(currentList)
            //thêm vào stack

            return currentList
        }

        //khi xoá kí tự thì autocomplete bằng cách pop stack
        fun autoCompleteRemove(): ArrayList<String> {
            return stackAutoComplete.pop() //lấy từ stack ra
        }
    }

    class People(userId: String) {

    }

    class Message(content: String, a: People, b: People) {

    }


}