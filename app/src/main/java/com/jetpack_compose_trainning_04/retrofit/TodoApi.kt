package com.jetpack_compose_trainning_04.retrofit

import com.jetpack_compose_trainning_04.Model
import com.jetpack_compose_trainning_04.ModelItem
import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {

    @GET("todos")
    suspend fun getTodos() : Response<List<ModelItem>>
}