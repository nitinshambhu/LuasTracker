package com.luas.tracker.forecast.data

import com.luas.tracker.forecast.api.ForecastApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ForecastRepository @Inject constructor(private val api: ForecastApi) {

    fun getForecast(stop: String): Single<StopInfo> = api.getForecast(stop = stop)
}