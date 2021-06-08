package com.kml.data.networking

import com.google.gson.GsonBuilder
import com.kml.data.networking.NetworkingConstants.TOKEN
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(NetworkingConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    fun provideApi(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor {
                val request: Request = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN").build()
                return@addInterceptor it.proceed(request)
            }
            .build()
    }

    private fun getGson() = GsonBuilder().setLenient().create()
}