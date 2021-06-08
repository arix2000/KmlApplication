package com.kml.di

import com.google.gson.GsonBuilder
import com.kml.data.networking.NetworkingConstants.BASE_URL
import com.kml.data.networking.RestApi
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { provideRetrofit() }
    factory { provideApi(get()) }
}

private fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .client(getOkHttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}

private fun provideApi(retrofit: Retrofit): RestApi {
    return retrofit.create(RestApi::class.java)
}

private fun getOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}

private fun getGson() = GsonBuilder().setLenient().create()