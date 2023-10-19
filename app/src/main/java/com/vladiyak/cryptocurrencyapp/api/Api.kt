package com.vladiyak.cryptocurrencyapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(" https://api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}