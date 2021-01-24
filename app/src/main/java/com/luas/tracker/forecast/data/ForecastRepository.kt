package com.luas.tracker.forecast.data

import com.luas.tracker.common.util.ApiResponse
import com.luas.tracker.common.util.apiResponseFrom
import com.luas.tracker.forecast.api.ForecastApi
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepository @Inject constructor(private val api: ForecastApi) {

    fun getForecast(stop: String): Single<StopInfo> = api.getForecast(stop = stop)

    suspend fun getForecastViaCoroutines(stop: String): ApiResponse<StopInfo> =
        withContext(Dispatchers.Default) {
            apiResponseFrom { api.getForecastViaCoroutines(stop = stop) }
        }
}