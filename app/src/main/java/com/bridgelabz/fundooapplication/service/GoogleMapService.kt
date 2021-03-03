package com.bridgelabz.fundooapplication.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapService {

    @GET("/maps/api/place/findplacefromtext/json")
    fun getSearchLocationsByName(@Query("key") key:String,
                                 @Query("input") placeName:String,
                                 @Query("inputtype") inputtype:String
    ) : Call<Any>

    companion object {
        fun getInstance() : GoogleMapService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com")
                .build()
            return retrofit.create(GoogleMapService::class.java)
        }
    }
}