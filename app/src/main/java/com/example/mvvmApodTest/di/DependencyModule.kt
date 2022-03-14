package com.example.mvvmApodTest


import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyModule {
    val mainActivityModule = module{
        //factory { ExampleFragment() }
    }
    val searchPictureModule = module{
        single { ApiClient.getApiRetrofitClient()?.create(ApiInterface::class.java) }
        factory { SearchPictureRepositoryImpl(get()) as SearchPictureRepository }
        factory { SearchPictureInteractorImpl() as SearchPictureInteractor }
        viewModel { SearchPictureViewModel2() }
        factory { SearchPictureAdapter() }
    }
}