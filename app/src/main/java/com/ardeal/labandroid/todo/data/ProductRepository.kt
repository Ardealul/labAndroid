package com.ardeal.labandroid.todo.data

import android.util.Log
import com.ardeal.labandroid.core.TAG
import com.ardeal.labandroid.todo.data.remote.ProductApi

object ProductRepository {
    private var cachedProducts: MutableList<Product>? = null;

    suspend fun loadAll(): List<Product> {
        Log.i(TAG, "loadAll")
        if (cachedProducts != null) {
            return cachedProducts as List<Product>;
        }
        cachedProducts = mutableListOf()
        val products = ProductApi.service.find()
        cachedProducts?.addAll(products)
        return cachedProducts as List<Product>
    }

    suspend fun load(productId: String): Product {
        Log.i(TAG, "load")
        val product = cachedProducts?.find { it.id == productId }
        if (product != null) {
            return product
        }
        return ProductApi.service.read(productId)
    }

    suspend fun save(product: Product): Product {
        Log.i(TAG, "save")
        val createdProduct = ProductApi.service.create(product)
        cachedProducts?.add(createdProduct)
        return createdProduct
    }

    suspend fun update(product: Product): Product {
        Log.i(TAG, "update")
        val updatedProduct = ProductApi.service.update(product.id, product)
        val index = cachedProducts?.indexOfFirst { it.id == product.id }
        if (index != null) {
            cachedProducts?.set(index, updatedProduct)
        }
        return updatedProduct
    }
}