package com.luas.tracker.forecast.data

import android.view.View
import io.mockk.*
import org.junit.Assert.*
import org.junit.Test
import androidx.databinding.library.baseAdapters.BR

class ForecastDataUiStateTest {

    fun getState(): ForecastDataUiState = spyk<ForecastDataUiState>().apply {
        every { notifyPropertyChanged(any()) } just Runs
    }

    @Test
    fun `Initial state of UI should display progress bar and hide list and error layouts`() {
        val uiState = getState()

        assertEquals(uiState.progressBarVisibility, View.VISIBLE)
        assertEquals(uiState.errorStateVisibility, View.GONE)
        assertEquals(uiState.listVisibility, View.GONE)

        assertFalse(uiState.showErrorState)
        assertFalse(uiState.showList)
    }

    @Test
    fun `When error occurs while displaying progress bar, error state becomes visible while list and progress bar are gone`() {
        val uiState = getState()

        uiState.showErrorState = true

        assertEquals(uiState.errorStateVisibility, View.VISIBLE)
        assertEquals(uiState.progressBarVisibility, View.GONE)
        assertEquals(uiState.listVisibility, View.GONE)

        assertTrue(uiState.showErrorState)
        assertFalse(uiState.showList)
        verify(exactly = 1) {
            uiState.run {
                notifyPropertyChanged(BR.progressBarVisibility)
                notifyPropertyChanged(BR.errorStateVisibility)
            }
        }
    }

    @Test
    fun `When data is received while displaying progress bar, list becomes visible while error state and progress bar are gone`() {
        val uiState = getState()

        uiState.showList = true

        assertEquals(uiState.listVisibility, View.VISIBLE)
        assertEquals(uiState.progressBarVisibility, View.GONE)
        assertEquals(uiState.errorStateVisibility, View.GONE)

        assertTrue(uiState.showList)
        assertFalse(uiState.showErrorState)
        verify(exactly = 1) {
            uiState.run {
                notifyPropertyChanged(BR.progressBarVisibility)
                notifyPropertyChanged(BR.listVisibility)
            }
        }
    }
}