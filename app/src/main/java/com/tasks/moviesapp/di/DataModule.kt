package com.tasks.moviesapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This Hilt module is responsible for providing setup data clients
 * such as Retrofit instances for network communication and
 * Room database instances for local data storage.
 *
 * All shared singletons related to data sources should be defined here.
 */

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

}