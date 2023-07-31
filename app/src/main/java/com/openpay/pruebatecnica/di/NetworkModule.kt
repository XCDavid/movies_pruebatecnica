package com.openpay.pruebatecnica.di

import android.util.Log
import com.openpay.pruebatecnica.provider.service.network.core.ApiClient
import com.openpay.pruebatecnica.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.openpay.pruebatecnica.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest = chain.request()
//                val token = Constants.ACCES_TOKEN
                val token = BuildConfig.ACCES_TOKEN
                val requestWithHeader = originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
                chain.proceed(requestWithHeader)
            }.addInterceptor{chain ->
                /**Log de envio body*/
                /**
                val originalRequest = chain.request()
                val requestBody = originalRequest.body()
                val buffer = Buffer()
                requestBody?.writeTo(buffer)
                val strOldBody = buffer.readUtf8()
                Log.i("body interceptor", strOldBody)
                val newRequest = originalRequest.newBuilder().method(originalRequest.method(),  originalRequest.body()).build()
                chain.proceed(newRequest)
                */
                val response = chain.proceed(chain.request())
                var responseBody = if (response.body() != null) response.body()!!.string() else "Cuerpo vacio"
                if (BuildConfig.LOGS_ENABLED)
                    Log.d("body interceptor", responseBody)
                 response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), responseBody)).build()
        }.build()

        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}