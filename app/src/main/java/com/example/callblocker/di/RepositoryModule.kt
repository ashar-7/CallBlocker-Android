package com.example.callblocker.di

import com.example.callblocker.repository.BlockListRepository
import com.example.callblocker.repository.BlockListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindBlockListRepository(
        blockListRepositoryImpl: BlockListRepositoryImpl
    ): BlockListRepository
}
