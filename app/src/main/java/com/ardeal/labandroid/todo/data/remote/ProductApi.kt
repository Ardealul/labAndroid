package com.ardeal.labandroid.todo.data.remote

import com.ardeal.labandroid.todo.data.Product
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ProductApi {
    private const val URL = "http://192.168.0.103:3000/"

    interface Service {
        @GET("/product")
        suspend fun find(): List<Product>

        @GET("/product/{id}")
        suspend fun read(@Path("id") productId: String): Product

        @Headers("Content-Type: application/json")
        @POST("/product")
        suspend fun create(@Body product: Product): Product

        @Headers("Content-Type: application/json")
        @PUT("/product/{id}")
        suspend fun update(@Path("id") productId: String, @Body product: Product): Product
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}