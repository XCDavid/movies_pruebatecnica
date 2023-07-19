package com.openpay.pruebatecnica.di

import com.openpay.pruebatecnica.provider.service.network.core.ApiClient
import com.openpay.pruebatecnica.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = Constants.ACCES_TOKEN
                val requestWithHeader = originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
                chain.proceed(requestWithHeader)
            }.build()

        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}