package com.example.mvvmApodTest


data class ViewState(
    val choseYear: String,
    val choseMonth: String,
    val choseDay: String,
    val choseDataDataBase:String,
    val pictureOfDay: PictureOfDayModel,
    val isLoading:Boolean,
    val errorMessage: String,
    val listWithDate: List<String>?,
    val itemClick:Boolean
)
