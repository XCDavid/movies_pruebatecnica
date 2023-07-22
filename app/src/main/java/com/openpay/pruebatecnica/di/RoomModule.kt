package com.openpay.pruebatecnica.di

import android.content.Context
import androidx.room.Room
import com.openpay.pruebatecnica.data.database.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    private val databaseName  = "movie_database"

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDatabase::class.java, databaseName ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: RoomDatabase) = db.getUserDAO()
    @Singleton
    @Provides
    fun provideProfileDao(db: RoomDatabase) = db.getProfileDAO()

    @Singleton
    @Provides
    fun provideRatedTVDao(db: RoomDatabase) = db.getRatedTVDAO()

    @Singleton
    @Provides
    fun provideRatedMovieDao(db: RoomDatabase) = db.getRatedMovieDAO()
    @Singleton
    @Provides
    fun providePopularMovieDao(db: RoomDatabase) = db.getPopularMovieDAO()

    @Singleton
    @Provides
    fun provideTopRatedMovieDao(db: RoomDatabase) = db.getTopRatedMovieDAO()

    @Singleton
    @Provides
    fun provideTrendingMovieDao(db: RoomDatabase) = db.getTrendingMovieDAO()
}