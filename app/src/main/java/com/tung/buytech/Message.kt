package com.tung.buytech

import com.google.type.DateTime

class Message(private var content: List<MessageContent>, private var person1: String, private var person2: String, private var product: Product) {
    val getContent: List<MessageContent>
        get() = content

    val getPerson1: String
        get() = person1

    val getPerson2: String
        get() = person2

    val getProduct: Product
        get() = product
}

class MessageContent(val text: String, val senderId: String, val recipientId: String, val time: DateTime)