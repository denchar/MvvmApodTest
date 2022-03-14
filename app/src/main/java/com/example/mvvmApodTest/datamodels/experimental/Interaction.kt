package com.example.mvvmApodTest

sealed class Interaction {
    data class YearChange(val year:String):Interaction()
    data class MonthChange(val month:String):Interaction()
    data class DayChange(val day:String):Interaction()
    data class DataDataBaseChange(val dataDataBase:String):Interaction()
    data class ErrorMessageChanged(val errorMessage :String) : Interaction()
    data class ItemClick(val itemClick :Boolean) : Interaction()

    object SearchPictureClick : Interaction()
    object AddPictureOfDayInDataBaseClick : Interaction()
    object DeleteItemInDBClick : Interaction()


}
