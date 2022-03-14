package com.example.mvvmApodTest

import io.reactivex.Completable
import io.reactivex.Single

interface SearchPictureRepository {
    fun searchPictureOfDay(date: String): Single<PictureOfDayModel>
    fun deleteItemInDB(date: String): Completable
    fun addPictureOfDayInDataBase(): Completable
}