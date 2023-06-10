package com.tung.buytech

import java.util.LinkedList

class AppController {
    companion object {
        @JvmField
        val productList: LinkedList<Product> = LinkedList()
        var userId: String = String() //chứa userID
    }
    class Product(name: String, price: Int, imageFile: String ){
        public var name: String = name
        public var price: Int = price
        public var imageFile: String = imageFile
        //khúc này là constructor của class

    }



}