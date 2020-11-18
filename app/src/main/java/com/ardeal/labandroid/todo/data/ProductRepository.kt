package com.ardeal.labandroid.todo.data

import com.ardeal.labandroid.todo.data.remote.ProductApi
import com.ardeal.labandroid.core.Result

object ProductRepository {
    private var cachedProducts: MutableList<Product>? = null;

    suspend fun loadAll(): Result<List<Product>> {
        if (cachedProducts != null) {
            return Result.Success(cachedProducts as List<Product>)
        }
        try {
            val products = ProductApi.service.find()
            cachedProducts = mutableListOf()
            cachedProducts?.addAll(products)
            return Result.Success(cachedProducts as List<Product>)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun load(productId: String): Result<Product> {
        val product = cachedProducts?.find { it._id == productId }
        if (product != null) {
            return Result.Success(product)
        }
        try {
            return Result.Success(ProductApi.service.read(productId))
        } catch (e: Exception) {
        return Result.Error(e)
        }
    }

    suspend fun save(product: Product): Result<Product> {
        try {
            val createdProduct = ProductApi.service.create(product)
            cachedProducts?.add(createdProduct)
            return Result.Success(createdProduct)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(product: Product): Result<Product> {
        try {
            val updatedProduct = ProductApi.service.update(product._id, product)
            val index = cachedProducts?.indexOfFirst { it._id == product._id }
            if (index != null) {
                cachedProducts?.set(index, updatedProduct)
            }
            return Result.Success(updatedProduct)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}