package com.kingpowerclick.android.auth0

import android.content.Context
import com.kingpowerclick.android.auth0.Util.getMetadataValue
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    fun getInstance(context: Context): Retrofit {
        val scheme = getMetadataValue("auth0Scheme", context)
        val domain = getMetadataValue("auth0Domain", context)
        val baseUrl = "$scheme://$domain"
        val client = OkHttpClient()
        val clientBuilder: OkHttpClient.Builder =
            client.newBuilder().addInterceptor(createInterceptor())

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
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
