package com.melikegoren.excitingspace.data.remote.model

data class ApodPhotoModel(
    val date: String,
    val explanation: String,
    val hdurl: String? = null,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)

data class ApodVideoModel(
    val date: String,
    val explanation: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)


