package com.luas.tracker.forecast.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luas.tracker.common.BaseViewModel
import com.luas.tracker.common.util.*
import com.luas.tracker.forecast.data.*
import kotlinx.coroutines.launch

class ForecastViewModel @ViewModelInject constructor(
    private val repo: ForecastRepository,
    private val dateTimeHelper: DateTimeHelper
) :
    BaseViewModel() {

    var uiState = ForecastDataUiState()
        private set

    val generalStatusMessage = MutableLiveData<String>()
    val stopInfoMessage = MutableLiveData<String>()

    private val _fetchLatestForecastTrams = MutableLiveData<MutableList<Tram>>()
    val latestForcecastTrams: LiveData<MutableList<Tram>> = _fetchLatestForecastTrams

    fun fetchForecast() {
        val stop =
            if (dateTimeHelper.firstHalfOfTheDay()) Constants.Marlborough else Constants.Stillorgan
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

    fun intendedDirection() =
        if (dateTimeHelper.firstHalfOfTheDay()) Constants.Outbound else Constants.Inbound

    fun updateUiState(state: LoadingState) {
        uiState.apply {
            showList = state.showInfo
            showErrorState = state.showError
        }
    }

    // Fetch info using Coroutines
    fun fetchForecastViaCoroutines() {
        viewModelScope.launch {

            val stop =
                if (dateTimeHelper.firstHalfOfTheDay()) Constants.Marlborough else Constants.Stillorgan

            apiResponseHandler(
                status = repo.getForecastViaCoroutines(stop = stop),
                onSuccess = { stopInfo ->
                    emitMessages(stopInfo)
                    handleTramForecastInfo(stopInfo.directions)
                },
                onError = { handleForecastError(it) }
            )
        }
    }

    fun refreshViaCoroutines() {
        updateUiState(LoadingState.InProgress)
        fetchForecastViaCoroutines()
    }
}