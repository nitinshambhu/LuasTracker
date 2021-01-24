package com.luas.tracker.forecast.api

import com.luas.tracker.forecast.data.StopInfo
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("/xml/get.ashx")
    fun getForecast(
        @Query("action") action: String = "forecast",
        @Query("encrypt") encrypt: Boolean = false,
        @Query("stop") stop: String
    ): Single<StopInfo>

    @GET("/xml/get.ashx")
    suspend fun getForecastViaCoroutines(
        @Query("action") action: String = "forecast",
        @Query("encrypt") encrypt: Boolean = false,
        @Query("stop") stop: String
    ): StopInfo
}