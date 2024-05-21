package com.example.ejercicio13

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAPP {
    companion object{
        fun getRetrofit(): Retrofit{return Retrofit.Builder().baseUrl(IContacto.url).addConverterFactory(GsonConverterFactory.create()).build()

        }
    }
}