package com.luas.tracker.forecast.di

import com.luas.tracker.forecast.api.ForecastApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ForecastModule {

    @Provides
    @Singleton
    fun providesForecastApi(retrofit: Retrofit) = retrofit.create(ForecastApi::class.java)

}