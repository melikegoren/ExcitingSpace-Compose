package com.melikegoren.excitingspace.data.remote.dto

import com.melikegoren.excitingspace.data.local.ApodEntity
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

fun Apod.toApodEntityPhoto() =
    ApodEntity(
        date = this.date,
        explanation = this.explanation,
        hdurl = this.hdurl,
        media_type = this.media_type,
        service_version = this.service_version,
        title = this.title,
        url = this.url
    )

fun Apod.toApodEntityVideo() =
    ApodEntity(
        date = this.date,
        explanation = this.explanation,
        hdurl = "",
        media_type = this.media_type,
        service_version = this.service_version,
        title = this.title,
        url = this.url
    )
