package com.example.mvvmApodTest


import io.reactivex.subjects.BehaviorSubject

class Values() {
    companion object {
        const val tag = " My Values Class"
        const val API_KEY = "biPKsmbnEKINusgDXcBz0GTCSNwVVQMNh8sGqjkJ"
        const val Translate_API_KEY =
            "trnsl.1.1.20200102T212837Z.c08d983f6579dd4a.873869cb2c743f4560e7c2f944d5780f16262c3b"

        var pictureOfDayModel: PictureOfDayModel =
            PictureOfDayModel("", "", "", "", "", "", "", "")
        var pictureOfDayWhoNeedAddInDB: PictureOfDayModel =
            PictureOfDayModel("", "", "", "", "", "", "", "")
        var listWithDate :BehaviorSubject<List<String>> = BehaviorSubject.create()
    }
}