package com.example.listingapp.network

import com.example.listingapp.WeatherModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://randomuser.me/"
private const val WEATHER_URL = "https://api.openweathermap.org/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val interceptor = run {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.apply {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}

val okHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
    .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
    .writeTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

private val retrofitWeather = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(WEATHER_URL)
    .build()

object ObjectUsersApi {
    val retrofitService : UsersApi by lazy {
        retrofit.create(UsersApi::class.java)
    }
    val retrofitServiceWeather : UsersApi by lazy {
        retrofitWeather.create(UsersApi::class.java)
    }
}

interface UsersApi {

    @GET("api")
    fun getPropertiesAsync(
        @Query("results")
        results: String = "25"
    ):
            Deferred<Response<NetworkContainer>>

    @GET("data/2.5/weather")
    fun getRandomUsersWeatherAsync(
        @Query("lat")
        lat : String,
        @Query("lon")
        lon : String,
        @Query("appid")
        appId : String
    ):
            Deferred<Response<WeatherModel>>

}
