package com.example.mvvmApodTest

import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

interface SearchPictureInteractor {
    fun addPictureOfDayInDataBase(): Completable
    fun deleteItemInDB(date: String): Completable
    fun searchPictureOfDay(date: String): Single<PictureOfDayModel>
}
class SearchPictureInteractorImpl : SearchPictureInteractor, KoinComponent{
    private val searchPictureRepository:SearchPictureRepository by inject()

    override fun addPictureOfDayInDataBase(): Completable {
        return searchPictureRepository.addPictureOfDayInDataBase()
    }

    override fun deleteItemInDB(date: String): Completable {
        return searchPictureRepository.deleteItemInDB(date)
    }

    override fun searchPictureOfDay(date: String): Single<PictureOfDayModel> {
        return searchPictureRepository.searchPictureOfDay(date)
    }

}