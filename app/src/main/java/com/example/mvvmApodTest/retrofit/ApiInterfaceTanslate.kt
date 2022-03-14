package com.example.mvvmApodTest

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterfaceTanslate {

    @GET("tr.json/translate?")
    fun translateText(
        @Query("key") key: String,
        @Query("text") text: String,
        @Query("lang") lang: String
    ): Single<Response<TranslateResponceModel>>
}