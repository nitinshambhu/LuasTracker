package com.luas.tracker.forecast.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luas.tracker.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}