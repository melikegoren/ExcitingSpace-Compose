package com.melikegoren.excitingspace.data.remote

import com.melikegoren.excitingspace.data.remote.dto.Apod
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApodApiService {

    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String): Apod

}