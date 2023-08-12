package com.tung.buytech.objects

import com.tung.buytech.control.AppController
import java.util.*
import kotlin.collections.ArrayList

class AutoComplete {
    lateinit var stackAutoComplete: Stack<ArrayList<String>>
    //stack là để phòng trường hợp người dùng xoá từ thì phải pop ra để lấy lại cái vừa trc đó

    init {
        lateinit var currentListOfTagsAndKeywords: ArrayList<String>

        //duyệt trong collection Products
        val collectionRef = AppController.getDatabaseInstance().collection("Products").get()

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
