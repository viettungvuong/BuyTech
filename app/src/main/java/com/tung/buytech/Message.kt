package com.tung.buytech

import com.google.type.DateTime

class Message(private var content: List<String>, private var senderId: String, private var receipientId: String, private var product: Product) {
}