package com.example.mvvmApodTest

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("planetary/apod?")
    fun getPictureOfDay(
        @Query("date") date: String,
        @Query("api_key") key: String
    ): Single<Response<PictureOfDayModel>>
//
    // https://api.nasa.gov/planetary/apod?api_key=biPKsmbnEKINusgDXcBz0GTCSNwVVQMNh8sGqjkJ

    @GET("tr.json/translate?")
    fun translateText(
        @Query("key") key: String,
        @Query("text") text: String,
        @Query("lang") lang: String
    ): Single<Response<TranslateResponceModel>>
}