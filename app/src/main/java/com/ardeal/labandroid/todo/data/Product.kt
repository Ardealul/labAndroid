package com.ardeal.labandroid.todo.data

data class Product(
        val _id: String,
        var name: String,
        var description: String,
        var price: String
) {
    override fun toString(): String = name
}