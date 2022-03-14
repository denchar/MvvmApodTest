package com.example.mvvmApodTest

import android.app.Application
import com.example.mvvmApodTest.DependencyModule.mainActivityModule
import com.example.mvvmApodTest.DependencyModule.searchPictureModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Mvvm3App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Mvvm3App)
            modules(listOf(mainActivityModule,searchPictureModule))
        }
    }

}