package com.example.fairshare.di

import android.content.Context
import androidx.room.Room
import com.example.fairshare.data.FairShareDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): FairShareDatabase {
        return Room.databaseBuilder(
            context,
            FairShareDatabase::class.java,
            "fairshare_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBucketDao(database: FairShareDatabase) = database.bucketDao()

    @Provides
    fun provideTaskDao(database: FairShareDatabase) = database.taskDao()
}
