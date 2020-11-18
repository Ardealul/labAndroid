package com.ardeal.labandroid.todo.data.remote

import com.ardeal.labandroid.core.Api
import com.ardeal.labandroid.todo.data.Product
import retrofit2.http.*

object ProductApi {
    interface Service {
        @GET("/api/product")
        suspend fun find(): List<Product>

        @GET("/api/product/{id}")
        suspend fun read(@Path("id") productId: String): Product

        @Headers("Content-Type: application/json")
        @POST("/api/product")
        suspend fun create(@Body product: Product): Product

        @Headers("Content-Type: application/json")
        @PUT("/api/product/{id}")
        suspend fun update(@Path("id") productId: String, @Body product: Product): Product
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}