package com.tasks.moviesapp.di

import com.tasks.moviesapp.data.repository.MovieRepositoryImp
import com.tasks.moviesapp.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(movieRepositoryImp: MovieRepositoryImp): MovieRepository

}