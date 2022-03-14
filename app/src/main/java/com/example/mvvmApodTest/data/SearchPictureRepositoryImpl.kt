package com.example.mvvmApodTest

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class SearchPictureRepositoryImpl(
    private val api: ApiInterface
) : SearchPictureRepository {
    private val compositeDisposable = CompositeDisposable()
    private val tag = this.javaClass.name

    private var inDataBase=ArrayList<String>()
    init {
        addPictureOfDayInDataBase()
    }

    fun search(date: String) {
        compositeDisposable.add(
            api.getPictureOfDay(date, Values.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d(tag, "${it.code()}") }, { })
        )
    }

    @SuppressLint("CheckResult")
    fun addPictureOfDayInDB() {
        Observable.just(Values.pictureOfDayModel)
            .map { addInDB(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(tag, "wright in db ${it.title}")
                }, {}, {}
            )
    }

    private fun addInDB(it: PictureOfDayModel): PictureOfDayModel {
        Log.d(tag, " addInDB wright in db ${it.title}")

        return it
    }

    @SuppressLint("CheckResult")
    override fun searchPictureOfDay(date: String): Single<PictureOfDayModel> {
        return api.getPictureOfDay(date, Values.API_KEY)
            .flatMap { response ->
                Log.d(this.javaClass.name, "response code ${response.code()}")
                when (response.code()) {
                    200 -> {
                        return@flatMap Single.just(response.body())
                    }
                    else -> throw Exception("Not success response code: ${response.code()}")
                }
            }

    }

    @SuppressLint("CheckResult")
    override fun deleteItemInDB(date: String): Completable {
        Log.d(this.javaClass.name, " remove value in Values.listWithDate")
        inDataBase.remove(date)
        Values.listWithDate.onNext(inDataBase)
        return Completable.complete()
    }

    override fun addPictureOfDayInDataBase(): Completable {
        Log.d(this.javaClass.name, " add value in Values.listWithDate")
        inDataBase.add(Values.pictureOfDayWhoNeedAddInDB.date)
        Values.listWithDate.onNext(inDataBase)
        return Completable.complete()

    }
}