package com.luas.tracker.forecast.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luas.tracker.common.BaseViewModel
import com.luas.tracker.common.util.Constants
import com.luas.tracker.common.util.LoadingState
import com.luas.tracker.common.util.applySingleSchedulers
import com.luas.tracker.common.util.firstHalfOfTheDay
import com.luas.tracker.forecast.data.*

class ForecastViewModel @ViewModelInject constructor(private val repo: ForecastRepository) :
    BaseViewModel() {

    var uiState = ForecastDataUiState()
        private set

    val generalStatusMessage = MutableLiveData<String>()
    val stopInfoMessage = MutableLiveData<String>()

    private val _fetchLatestForecastTrams = MutableLiveData<MutableList<Tram>>()
    val latestForcecastTrams: LiveData<MutableList<Tram>> = _fetchLatestForecastTrams

    fun fetchForecast() {
        val stop = if (firstHalfOfTheDay()) Constants.Marlborough else Constants.Stillorgan
        addDisposable(
            repo.getForecast(stop = stop)
                .applySingleSchedulers()
                .doAfterSuccess(this::emitMessages)
                .map { it.directions }
                .subscribe(this::handleTramForecastInfo, this::handleForecastError)
        )
    }

    fun refresh() {
        updateUiState(LoadingState.InProgress)
        fetchForecast()
    }

    fun handleTramForecastInfo(directions: MutableList<Direction>) {
        val intendedDirection = intendedDirection()
        val trams = directions.first { it.name == intendedDirection }.trams
        updateUiState(LoadingState.Success)
        _fetchLatestForecastTrams.value = trams
    }

    fun handleForecastError(throwable: Throwable) {
        updateUiState(LoadingState.Error)
        throwable.printStackTrace()
    }

    fun emitMessages(stopInfo: StopInfo) {
        generalStatusMessage.value = stopInfo.message
        stopInfoMessage.value = "From ${stopInfo.stop.toUpperCase()} - ${intendedDirection()}"
    }

    fun intendedDirection() = if (firstHalfOfTheDay()) Constants.Outbound else Constants.Inbound

    fun updateUiState(state : LoadingState) {
        uiState.apply {
            showList = state.showInfo
            showErrorState = state.showError
        }
    }
}