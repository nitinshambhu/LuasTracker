package com.luas.tracker.forecast.data

import com.luas.tracker.R
import org.junit.Assert.assertEquals
import org.junit.Test

class TramTest {

    @Test
    fun `DueMin field being set to Due should set the background color to green`() {
        val tram = Tram("Due", "")
        assertEquals(R.color.green, tram.backgroundColor)
        assertEquals(R.color.white, tram.textColor)
    }

    @Test
    fun `Destination field set to Broombridge should set the background color to darkOrange`() {
        val tram = Tram("7", "Broombridge")
        assertEquals(R.color.darkOrange, tram.backgroundColor)
        assertEquals(R.color.black, tram.textColor)
    }

    @Test
    fun `Destination field set to Parnell should set the background color to lightOrange`() {
        val tram = Tram("7", "Parnell")
        assertEquals(R.color.lightOrange, tram.backgroundColor)
        assertEquals(R.color.black, tram.textColor)
    }

    @Test
    fun `Destination field set to Sandyford should set the background color to lightOrange`() {
        val tram = Tram("7", "Sandyford")
        assertEquals(R.color.lightOrange, tram.backgroundColor)
        assertEquals(R.color.black, tram.textColor)
    }

    @Test
    fun `Destination field set to BridesGlen should set the background color to darkOrange`() {
        val tram = Tram("7", "Bride's Glen")
        assertEquals(R.color.darkOrange, tram.backgroundColor)
        assertEquals(R.color.black, tram.textColor)
    }
}