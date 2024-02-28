package com.melikegoren.excitingspace.ui.screens.home

import com.melikegoren.excitingspace.data.remote.model.ApodPhotoModel
import com.melikegoren.excitingspace.data.remote.model.ApodVideoModel

data class HomeScreenUiModelPhoto (
    val title: String,
    val explanation: String,
    val url: String,
    val serviceVersion: String = "photo"
)

data class HomeScreenUiModelVideo (
    val title: String,
    val explanation: String,
    val url: String,
    val serviceVersion: String = "video"

)

fun ApodPhotoModel.toHomeScreenUiModelPhoto() = HomeScreenUiModelPhoto(
    title = this.title,
    explanation = this.explanation,
    url = this.url,
    serviceVersion = this.serviceVersion
)

fun ApodVideoModel.toHomeScreenUiModelVideo() = HomeScreenUiModelPhoto(
    title = this.title,
    explanation = this.explanation,
    url = this.url,
    serviceVersion = this.serviceVersion
)
