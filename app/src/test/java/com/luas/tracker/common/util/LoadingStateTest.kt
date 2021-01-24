package com.luas.tracker.common.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoadingStateTest {

    @Test
    fun `InProgress state should have both showInfo and showError set to false`() {
        assertFalse(LoadingState.InProgress.showInfo)
        assertFalse(LoadingState.InProgress.showError)
    }

    @Test
    fun `Success state should have both showInfo set to true and showError set to false`() {
        assertTrue(LoadingState.Success.showInfo)
        assertFalse(LoadingState.Success.showError)
    }

    @Test
    fun `Error state should have both showInfo set to false and showError set to true`() {
        assertFalse(LoadingState.Error.showInfo)
        assertTrue(LoadingState.Error.showError)
    }
}