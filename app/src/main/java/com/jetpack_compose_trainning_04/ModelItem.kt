package com.jetpack_compose_trainning_04

data class ModelItem(
    var completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
)