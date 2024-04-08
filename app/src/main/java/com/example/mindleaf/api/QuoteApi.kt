package com.example.mindleaf.api

import com.example.mindleaf.data.Quote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote(): Quote

    companion object{
        private const val BASE_URL = "https://api.quotable.io/"

        val instance:QuoteApi by lazy{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(QuoteApi::class.java)
        }
    }
}