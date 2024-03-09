package com.melikegoren.excitingspace.data.repository

import android.content.Context
import coil.compose.AsyncImagePainter
import com.melikegoren.excitingspace.data.remote.model.ApodPhotoModel
import com.melikegoren.excitingspace.data.remote.model.ApodVideoModel
import kotlinx.coroutines.flow.Flow

interface ApodRepository {

    suspend fun getApodWithPhoto(apiKey: String): Flow<ApodPhotoModel>

    suspend fun getApodWithVideo(apiKey: String): Flow<ApodVideoModel>

    suspend fun saveImageToGallery(painter: AsyncImagePainter, context: Context, url: String, title: String)


}