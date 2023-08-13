package com.tung.buytech.objects

import com.tung.buytech.control.AppController
import com.tung.buytech.control.AppController.Companion.bindProductById
import com.tung.buytech.control.AppController.Companion.db
import com.tung.buytech.control.collectionProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

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

class ProductRepository(private val product: Product){
    fun updateProductSold(): Flow<Product> = flow{
        withContext(Dispatchers.IO){
            db.collection(collectionProducts).document(product.productId).get()
                .addOnSuccessListener {
                    document ->
                    val stock = (document.getLong("stock")?:0).toInt()
                    if (stock<=0){
                        product.sold=true
                    }
                }

            emit(product) //phát ra product từ flow
        }

    }
}