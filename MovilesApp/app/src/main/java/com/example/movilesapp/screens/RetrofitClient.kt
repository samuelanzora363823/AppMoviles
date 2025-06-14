package com.example.movilesapp.api

import com.example.movilesapp.screens.RutasApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://sqlserver-restapi.onrender.com/" // Cambia si usas localhost

    val api: RutasApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(RutasApi::class.java)
    }
}
