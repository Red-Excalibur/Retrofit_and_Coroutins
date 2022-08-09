package com.jetpack_compose_trainning_04.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api : TodoApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TodoApi::class.java)
    }
    //now we can use the api instance to make our requests
}