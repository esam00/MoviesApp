package com.tasks.moviesapp.di

import com.tasks.moviesapp.data.remote.ApiConstants
import com.tasks.moviesapp.data.remote.MoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This Hilt module is responsible for providing setup data clients
 * such as Retrofit instances for network communication and
 * Room database instances for local data storage.
 *
 * All shared singletons related to data sources should be defined here.
 */

@Module
@InstallIn(SingletonComponent::class)
class RetrofitDataModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDefaultApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }

}