package com.tung.buytech.objects

import com.tung.buytech.control.AppController
import com.tung.buytech.control.AppController.Companion.bindProductById
import com.tung.buytech.control.AppController.Companion.db
import com.tung.buytech.control.collectionProducts
import com.tung.buytech.fragments.Favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.LinkedList

open class Product(var name: String, var price: Long, var imageUrl: String, var productId: String) :
    java.io.Serializable {


    var sold = false //đã bán hay chưa


}

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
}

class FavoritesRepository(private val favorites: LinkedList<Favorite>){

    suspend fun updateProduct(){
        withContext(Dispatchers.IO){
            for (favorite in favorites){
                db.collection(collectionProducts).document(favorite.productId).get()
                    .addOnSuccessListener {
                            document ->
                        val stock = (document.getLong("stock")?:0).toInt()
                        if (stock<=0){
                            favorite.sold=true //đánh dấu đã bán rồi
                        }
                    }
            }
        }

    }
}