package com.tung.buytech

import java.util.LinkedList

class AppController {
    class Product(name: String, price: Int, imageFile: String ){
        public var name: String = name
        public var price: Int = price
        public var iamgeFile: String = imageFile
        //khúc này là constructor của class

        fun getName(): String{
            return this.name
        }

        fun getPrice(): Int{
            return this.price;
        }

        fun getImage(): String{
            return this.iamgeFile
        }
    }


    var personList: LinkedList<Product> = LinkedList()

}