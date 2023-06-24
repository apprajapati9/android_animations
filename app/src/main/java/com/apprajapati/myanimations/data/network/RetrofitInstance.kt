package com.apprajapati.myanimations.data.network

import com.apprajapati.myanimations.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder().build()

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(Constants.BASE_URL).
            client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TimeApi by lazy {
        retrofit.create(TimeApi::class.java)
    }
}