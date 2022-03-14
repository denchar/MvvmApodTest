package com.example.mvvmApodTest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.get

class SearchPictureViewModel2 : ViewModel(),  KoinComponent {

    private val searchPictureInteractor: SearchPictureInteractor = get()
    private val compositeDisposable = CompositeDisposable()

    private var currentViewState = ViewState(
        choseYear = "1995",
        choseMonth = "06",
        choseDay = "20",
        choseDataDataBase = "",
        pictureOfDay = PictureOfDayModel("", "", "", "", "", "", "", ""),
        isLoading = false,
        errorMessage = "",
        listWithDate = listOf(""),
        itemClick = false
    )
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        Values.listWithDate
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    Log.d(this.javaClass.name, " add value in Values.listWithDate")
                    submitViewState(currentViewState.copy(listWithDate = it))
                },
                onError = { Log.d(this.javaClass.name, " onError Values.listWithDate") },
                onComplete = { Log.d(this.javaClass.name, " onComplete Values.listWithDate") })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun submitViewState(viewState: ViewState) {
        currentViewState = viewState
        _viewState.value = currentViewState
    }

    fun onInteraction(interaction: Interaction) {
        when (interaction) {
            is Interaction.YearChange -> setYear(interaction.year)
            is Interaction.MonthChange -> setMonth(interaction.month)
            is Interaction.DayChange -> setDay(interaction.day)
            is Interaction.ErrorMessageChanged -> setErrorValue(interaction.errorMessage)
            is Interaction.DataDataBaseChange -> setDataDataBase(interaction.dataDataBase)
            is Interaction.SearchPictureClick -> searchPicture()
            is Interaction.AddPictureOfDayInDataBaseClick -> addPictureOfDayInDataBase()
            is Interaction.DeleteItemInDBClick -> deleteItemInDB()
            is Interaction.ItemClick -> itemWasClick(interaction.itemClick)
        }
    }

    private fun itemWasClick(value: Boolean){
        submitViewState(currentViewState.copy(itemClick = value))
    }

    private fun searchPicture() {
        submitViewState(currentViewState.copy(isLoading = true))
        searchPictureInteractor.searchPictureOfDay(
            "${currentViewState.choseYear}-${currentViewState.choseMonth}-${currentViewState.choseDay}"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    submitViewState(currentViewState.copy(pictureOfDay = it))
                    submitViewState(currentViewState.copy(isLoading = false))
                    Log.d(this.javaClass.name, "it = ${it}")
                    Log.d(this.javaClass.name, "viewState = ${viewState.value?.pictureOfDay}")
                },
                onError = { submitViewState(currentViewState.copy(errorMessage = it.toString()))
                    submitViewState(currentViewState.copy(isLoading = false))}
            ).addTo(compositeDisposable)
    }

    private fun setErrorValue(value: String?) {
        if (value != null) {
            submitViewState(currentViewState.copy(errorMessage = value))
        }
    }

    private fun setYear(year: String) {
        submitViewState(currentViewState.copy(choseYear = year))
        Log.d(this.javaClass.name, "currentViewState = ${currentViewState.choseYear}")
        Log.d(this.javaClass.name, "_viewState = ${_viewState.value?.choseYear}")
        Log.d(this.javaClass.name, "viewState = ${viewState.value?.choseYear}")
    }

    private fun setMonth(month: String) {
        submitViewState(currentViewState.copy(choseMonth = month))
    }

    private fun setDay(day: String) {
        submitViewState(currentViewState.copy(choseDay = day))
    }

    private fun setDataDataBase(dataDataBase: String) {
        submitViewState(currentViewState.copy(choseDataDataBase = dataDataBase))
    }

    private fun addPictureOfDayInDataBase() {
        if (Values.listWithDate.value?.contains(currentViewState.choseDataDataBase) == false) {
            Log.d(this.javaClass.name, " add value in Values.listWithDate")
            searchPictureInteractor.addPictureOfDayInDataBase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {},
                    onComplete = {
                        Log.d(
                            this.javaClass.name,
                            " ${Values.pictureOfDayWhoNeedAddInDB.title} was write in database"
                        )
                    }).addTo(compositeDisposable)
        }
        Log.d(this.javaClass.name, """Values.listWithDate =${Values.listWithDate.value},
            | currentViewState.choseDataDataBase = ${currentViewState.choseDataDataBase}
        """.trimMargin())
    }

    private fun deleteItemInDB() {
        searchPictureInteractor.deleteItemInDB(currentViewState.choseDataDataBase)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {},
                onComplete = {
                    Log.d(
                        this.javaClass.name,
                        " ${currentViewState.choseDataDataBase} was delete from DataBase"
                    )
                }).addTo(compositeDisposable)
    }
}