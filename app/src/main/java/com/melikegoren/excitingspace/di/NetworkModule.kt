package com.melikegoren.excitingspace.di

import android.content.Context
import com.melikegoren.excitingspace.common.Constants.BASE_URL
import com.melikegoren.excitingspace.data.remote.ApodApiService
import com.melikegoren.excitingspace.data.remote.CacheInterceptor
import com.melikegoren.excitingspace.data.remote.ForceCacheInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(File(context.cacheDir, "http-cache"), 10L * 1024L * 1024L)) // 10 MiB
        .addNetworkInterceptor(CacheInterceptor())
        .addInterceptor(ForceCacheInterceptor(context))
        .build()
    @Provides
    @Singleton
    fun provideApodApi(@ApplicationContext context: Context) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ApodApiService::class.java)

}