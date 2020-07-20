package com.example.olhovivoestagio.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        private const val baseUrl = "http://api.olhovivo.sptrans.com.br/v2.1/"
        private val httpClient = OkHttpClient.Builder()
        private lateinit var retrofit: Retrofit
        private fun getRetrofitInstance(): Retrofit{

            if(!::retrofit.isInitialized){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun <S> createService(service: Class<S>) : S{
            return getRetrofitInstance().create(service)
        }


    }




}