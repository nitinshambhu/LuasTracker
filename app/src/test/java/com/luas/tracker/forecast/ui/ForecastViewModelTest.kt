package com.luas.tracker.forecast.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luas.tracker.common.CoroutineTestRule
import com.luas.tracker.common.util.*
import com.luas.tracker.forecast.data.Direction
import com.luas.tracker.forecast.data.ForecastRepository
import com.luas.tracker.forecast.data.StopInfo
import com.luas.tracker.forecast.data.Tram
import io.mockk.*
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ForecastViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    val repo: ForecastRepository = mockk()
    val dateTimeHelper: DateTimeHelper = mockk()
    val viewModel: ForecastViewModel = spyk(ForecastViewModel(repo, dateTimeHelper))

    @After
    fun tearDown() {
        unmockkObject(repo, viewModel)
    }

    @Test
    fun `Updating UI state with LoadingStateSuccess should set showList to true and showError to false`() {
        viewModel.updateUiState(LoadingState.Success)
        assertTrue(viewModel.uiState.showList)
        assertFalse(viewModel.uiState.showErrorState)
    }

    @Test
    fun `Updating UI state with LoadingStateError should set showList to false and showError to true`() {
        viewModel.updateUiState(LoadingState.Error)
        assertTrue(viewModel.uiState.showErrorState)
        assertFalse(viewModel.uiState.showList)
    }

    @Test
    fun `Updating UI state with LoadingStateInProgress should set showList to false and showError to false`() {
        viewModel.updateUiState(LoadingState.InProgress)
        assertFalse(viewModel.uiState.showErrorState)
        assertFalse(viewModel.uiState.showList)
    }

    @Test
    fun `Intended Direction should be Outbound if it is the first half of the day`() {
        every { dateTimeHelper.firstHalfOfTheDay() } returns true
        val direction = viewModel.intendedDirection()
        assertEquals(direction, Constants.Outbound)
    }

    @Test
    fun `Intended Direction should be Inbound if it is NOT the first half of the day`() {
        every { dateTimeHelper.firstHalfOfTheDay() } returns false
        val direction = viewModel.intendedDirection()
        assertEquals(direction, Constants.Inbound)
    }

    @Test
    fun `Emit messages should emit a general status message and the info about the selected stop`() {
        val stopInfo = StopInfo(message = "Test message", stop = "Ireland")
        every { viewModel.intendedDirection() } returns Constants.Outbound
        viewModel.emitMessages(stopInfo)
        assertEquals("Test message", viewModel.generalStatusMessage.value)
        assertEquals("From IRELAND - Outbound", viewModel.stopInfoMessage.value)
    }

    @Test
    fun `Handling error should update the ui state with LoadingStateError`() {
        val throwable: Throwable = mockk(relaxUnitFun = true)
        every { viewModel.updateUiState(any()) } just Runs
        viewModel.handleForecastError(throwable)
        verify { viewModel.updateUiState(LoadingState.Error) }
    }

    @Test
    fun `Handling Directions info should update the ui state with LoadingStateSuccess`() {
        val trams = mutableListOf<Tram>()
        val directions = mutableListOf(Direction(name = Constants.Outbound, trams = trams))
        every { viewModel.intendedDirection() } returns Constants.Outbound
        every { viewModel.updateUiState(any()) } just Runs

        viewModel.handleTramForecastInfo(directions)

        assertEquals(trams, viewModel.latestForcecastTrams.value)
        verify { viewModel.updateUiState(LoadingState.Success) }
    }

    @Test
    fun `Refresh method should update the UI with LoadingStateInProgress and also call fetchForecast method`() {
        every { viewModel.updateUiState(any()) } just Runs
        every { viewModel.fetchForecast() } just Runs

        viewModel.refresh()

        verifyOrder {
            viewModel.updateUiState(LoadingState.InProgress)
            viewModel.fetchForecast()
        }
    }

    @Test
    fun `Fetch Forecast method should call getForecast on Repo with Marlborough if it is first half of the day`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        val directions = mutableListOf<Direction>()
        val stopInfo: StopInfo = mockk()
        every { stopInfo.directions } returns directions
        every { dateTimeHelper.firstHalfOfTheDay() } returns true
        every { repo.getForecast(any()) } returns Single.just(stopInfo)
        every { viewModel.emitMessages(stopInfo) } just Runs
        every { viewModel.handleTramForecastInfo(any()) } just Runs

        viewModel.fetchForecast()

        verify(exactly = 1) {
            repo.getForecast(Constants.Marlborough)
            viewModel.emitMessages(stopInfo)
            viewModel.handleTramForecastInfo(directions)
        }
    }

    @Test
    fun `Fetch Forecast method should call getForecast on Repo with Stillorgan if it is NOT first half of the day`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        val directions = mutableListOf<Direction>()
        val stopInfo: StopInfo = mockk()
        every { stopInfo.directions } returns directions
        every { dateTimeHelper.firstHalfOfTheDay() } returns false
        every { repo.getForecast(any()) } returns Single.just(stopInfo)
        every { viewModel.emitMessages(stopInfo) } just Runs
        every { viewModel.handleTramForecastInfo(any()) } just Runs

        viewModel.fetchForecast()

        verify(exactly = 1) {
            repo.getForecast(Constants.Stillorgan)
            viewModel.emitMessages(stopInfo)
            viewModel.handleTramForecastInfo(directions)
        }
    }

    @Test
    fun `Fetch Forecast method should handle should fetch call throw any error`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val throwable: Throwable = mockk()
        every { dateTimeHelper.firstHalfOfTheDay() } returns false
        every { repo.getForecast(any()) } returns Single.error(throwable)
        every { viewModel.handleForecastError(throwable) } just Runs

        viewModel.fetchForecast()

        verify(exactly = 1) {
            repo.getForecast(Constants.Stillorgan)
            viewModel.handleForecastError(throwable)
        }

        verify(exactly = 0) { viewModel.emitMessages(any()) }
    }

    @Test
    fun `Handle success when fetchForecastViaCoroutines method returns successfully`() {
        val stopInfo: StopInfo = mockk()
        val directions: MutableList<Direction> = mockk()
        every { stopInfo.directions } returns directions
        coEvery { repo.getForecastViaCoroutines(any()) } returns stopInfo.asSuccess()
        every { dateTimeHelper.firstHalfOfTheDay() } returns true
        every { viewModel.emitMessages(stopInfo) } just Runs
        every { viewModel.handleTramForecastInfo(any()) } just Runs

        runBlockingTest {

            viewModel.fetchForecastViaCoroutines()

            coVerifyOrder {
                repo.getForecastViaCoroutines(Constants.Marlborough)
                viewModel.emitMessages(stopInfo)
                viewModel.handleTramForecastInfo(directions)
            }
        }
    }

    @Test
    fun `Handle Error when fetchForecastViaCoroutines method returns an error`() {
        val throwable: Throwable = mockk()
        coEvery { repo.getForecastViaCoroutines(any()) } returns throwable.asError()
        every { dateTimeHelper.firstHalfOfTheDay() } returns false
        every { throwable.printStackTrace() } just Runs

        runBlockingTest {

            viewModel.fetchForecastViaCoroutines()

            coVerifyOrder {
                repo.getForecastViaCoroutines(Constants.Stillorgan)
                viewModel.handleForecastError(throwable)
                viewModel.updateUiState(LoadingState.Error)
                throwable.printStackTrace()
            }
        }
    }
}