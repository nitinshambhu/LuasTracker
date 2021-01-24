package com.luas.tracker.forecast.data

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.luas.tracker.BR
import com.luas.tracker.R
import com.luas.tracker.common.util.Constants
import com.luas.tracker.common.util.visibilityIf
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "stopInfo", strict = false)
data class StopInfo @JvmOverloads constructor(
    @field: Attribute(name = "stop")
    var stop: String = "",
    @field: Attribute(name = "stopAbv")
    var stopAbv: String = "",
    @field: ElementList(inline = true, required = false)
    var directions: MutableList<Direction> = mutableListOf(),
    @field:Element(name = "message")
    var message: String = ""
)

@Root(name = "direction", strict = false)
data class Direction @JvmOverloads constructor(
    @field: Attribute(name = "name", required = false)
    var name: String = "",
    @field: ElementList(inline = true, required = false)
    var trams: MutableList<Tram> = mutableListOf()
)

@Root(name = "tram", strict = false)
data class Tram @JvmOverloads constructor(
    @field: Attribute(name = "dueMins")
    var dueMins: String = "",
    @field: Attribute(name = "destination")
    var destination: String = ""
) {
    val backgroundColor: Int
        get() = when {
            dueMins.equals("due", true) -> R.color.green
            Constants.finalStops.contains(destination) -> R.color.darkOrange
            else -> R.color.lightOrange
        }

    val textColor: Int
        get() = when {
            dueMins.equals("due", true) -> R.color.white
            else -> R.color.black
        }
}

data class ForecastDataUiState(
    @get: Bindable var progressBarVisibility: Int = View.VISIBLE,
    @get: Bindable var listVisibility: Int = View.GONE,
    @get: Bindable var errorStateVisibility: Int = View.GONE
) : BaseObservable() {

    var showErrorState: Boolean = false
        set(value) {
            field = value
            progressBarVisibility = visibilityIf(visible = !value && !showList)
            errorStateVisibility = visibilityIf(visible = value)
            notifyPropertyChanged(BR.progressBarVisibility)
            notifyPropertyChanged(BR.errorStateVisibility)
        }

    var showList: Boolean = false
        set(value) {
            field = value
            progressBarVisibility = visibilityIf(visible = !value && !showErrorState)
            listVisibility = visibilityIf(visible = value)
            notifyPropertyChanged(BR.listVisibility)
            notifyPropertyChanged(BR.progressBarVisibility)
        }

    var showNoTramsMessage: Boolean = false
}