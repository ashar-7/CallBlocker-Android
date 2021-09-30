package com.example.callblocker.di

import android.content.Context
import androidx.room.Room
import com.example.callblocker.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "database")
            .build()

    @Provides
    @Singleton
    fun provideBlockListDao(db: AppDatabase) = db.blockListDao()
}
