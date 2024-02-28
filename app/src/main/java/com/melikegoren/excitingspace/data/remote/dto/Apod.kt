package com.melikegoren.excitingspace.data.remote.dto

import com.melikegoren.excitingspace.data.remote.model.ApodPhotoModel
import com.melikegoren.excitingspace.data.remote.model.ApodVideoModel


data class Apod(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)

fun Apod.ToApodPhotoModel() =
    ApodPhotoModel(
        date = this.date,
        explanation = this.explanation,
        hdurl = this.hdurl,
        mediaType = this.media_type,
        serviceVersion = this.service_version,
        title = this.title,
        url = this.url
    )

fun Apod.toApodVideoModel() =
    ApodVideoModel(
        date = this.date,
        explanation = this.explanation,
        mediaType = this.media_type,
        serviceVersion = this.service_version,
        title = this.title,
        url = this.url
    )
