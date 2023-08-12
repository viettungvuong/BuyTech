package com.tung.buytech.objects

import com.tung.buytech.control.AppController

open class Person(var userId: String) {
    lateinit var name: String
    init {
        this.userId=userId
        //lấy user từ database
        AppController.db.collection("users").document(userId).get().addOnSuccessListener {
                document->
            this.name=document.getString("name").toString()
        }
    }


}

class PersonProduct(private var person: Person, private var product: Product){
    val getPerson: Person
        get() = person

    val getProduct: Product
        get() = product
}
