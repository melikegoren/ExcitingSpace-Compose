package com.melikegoren.excitingspace.data.repository

import android.util.Log
import com.melikegoren.excitingspace.data.remote.ApodApiService
import com.melikegoren.excitingspace.data.remote.dto.ToApodPhotoModel
import com.melikegoren.excitingspace.data.remote.dto.toApodVideoModel
import com.melikegoren.excitingspace.data.remote.model.ApodPhotoModel
import com.melikegoren.excitingspace.data.remote.model.ApodVideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApodRepositoryImpl @Inject constructor(
    private val network: ApodApiService
): ApodRepository {
    override suspend fun getApodWithPhoto(apiKey: String): Flow<ApodPhotoModel> = flow {
        val apod = network.getApod(apiKey)
        emit(apod.ToApodPhotoModel())
        Log.d("emittt", network.getApod(apiKey).ToApodPhotoModel().title)
    }.flowOn(Dispatchers.IO)

    override suspend fun getApodWithVideo(apiKey: String): Flow<ApodVideoModel> = flow {
        val apod = network.getApod(apiKey)
        emit(apod.toApodVideoModel())
    }.flowOn(Dispatchers.IO)


}