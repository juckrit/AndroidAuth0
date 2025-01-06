package com.kingpowerclick.android.auth0

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://dev.onepass.kpc-dev.com/"

    fun getInstance(): Retrofit {
        val client = OkHttpClient()
        val clientBuilder: OkHttpClient.Builder =
            client.newBuilder().addInterceptor(createInterceptor())

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

    private fun createInterceptor() =
        Interceptor { chain ->
            val request =
                chain
                    .request()
                    .newBuilder()
                    .header("content-type", "application/x-www-form-urlencoded")
                    .method(chain.request().method, chain.request().body)
            chain.proceed(request.build())
        }
}
