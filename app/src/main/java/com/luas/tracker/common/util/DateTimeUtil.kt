package com.luas.tracker.common.util

import java.util.*
import javax.inject.Inject

class DateTimeHelper @Inject constructor() {

    private val midNightAsDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 1)
    }.time

    private val midDayAsDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 1)
        set(Calendar.SECOND, 0)
    }.time

    fun firstHalfOfTheDay(): Boolean {
        val currentTime = Calendar.getInstance().time
        return currentTime.after(midNightAsDate) && currentTime.before(midDayAsDate)
    }
}