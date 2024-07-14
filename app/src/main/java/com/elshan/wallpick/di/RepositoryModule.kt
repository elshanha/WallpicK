package com.elshan.wallpick.di

import com.elshan.wallpick.main.domain.repository.MainRepository
import com.elshan.wallpick.main.data.repository.MainRepositoryImpl
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
    abstract fun bindMainRepository(repository: MainRepositoryImpl): MainRepository

}