package com.tung.buytech

import com.google.type.DateTime

class Message(private var content: List<String>, private var sender: Person, private var receipient: Person, private var product: Product) {
}