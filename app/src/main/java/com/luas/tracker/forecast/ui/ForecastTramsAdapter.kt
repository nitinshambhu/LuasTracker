package com.luas.tracker.forecast.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.luas.tracker.R
import com.luas.tracker.databinding.ListItemTramBinding
import com.luas.tracker.forecast.data.Tram
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ForecastTramsAdapter @Inject constructor(@ApplicationContext context: Context) :
    ArrayAdapter<Tram>(context, R.layout.list_item_tram) {

    private val trams: MutableList<Tram> = mutableListOf()

    fun changeList(list: MutableList<Tram>) {
        trams.clear()
        trams.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Tram? {
        return trams[position]
    }

    override fun getCount(): Int = trams.size

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        var currentView = view
        val viewHolder: ForecastTramViewHolder = if (currentView == null) {
            val inflater = LayoutInflater.from(context)
            val binding = ListItemTramBinding.inflate(inflater)
            currentView = binding.root
            ForecastTramViewHolder(binding)
        } else {
            currentView.tag as ForecastTramViewHolder
        }
        getItem(position)?.apply { viewHolder.bind(this) }
        currentView.tag = viewHolder
        return currentView
    }

    class ForecastTramViewHolder constructor(private val binding: ListItemTramBinding) {
        fun bind(tram: Tram) {
            binding.tram = tram
        }
    }
}