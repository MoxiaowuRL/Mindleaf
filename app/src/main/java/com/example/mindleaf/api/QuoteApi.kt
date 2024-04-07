package com.example.mindleaf.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote(): Quote

    object QuoteApiService {
        private const val BASE_URL = "https://api.quotable.io/"

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val quoteApi: QuoteApi = retrofit.create(QuoteApi::class.java)
    }
}