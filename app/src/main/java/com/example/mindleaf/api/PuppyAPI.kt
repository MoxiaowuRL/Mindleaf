package com.example.mindleaf.api

import com.example.mindleaf.data.PuppyImage
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PuppyApi {
    @GET("breeds/image/random")
    suspend fun getRandomPuppyImage(): PuppyImage

    companion object {
        private const val BASE_URL = "https://dog.ceo/api/"

        val instance: PuppyApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(PuppyApi::class.java)
        }
    }
}