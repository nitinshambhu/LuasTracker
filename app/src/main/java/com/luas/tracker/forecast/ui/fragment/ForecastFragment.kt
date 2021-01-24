package com.luas.tracker.forecast.ui.fragment

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.luas.tracker.R
import com.luas.tracker.databinding.FragmentForecastBinding
import com.luas.tracker.forecast.ui.ForecastTramsAdapter
import com.luas.tracker.forecast.ui.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private lateinit var binding: FragmentForecastBinding
    private val viewModel: ForecastViewModel by viewModels()

    @Inject
    lateinit var tramsListAdapter: ForecastTramsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.tramsList.adapter = tramsListAdapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.message.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.scroll_text_horizontally
            ) as Animation
        )

//        viewModel.latestForcecastMessage.observe(viewLifecycleOwner) { binding.message.text = it }
        viewModel.latestForcecastTrams.observe(viewLifecycleOwner) { tramsListAdapter.changeList(it) }

        /**
         *  We could have a check like this to avoid fetching the info every time the fragment
         *  gets recreated. However, this being an app that tracks realtime info, I chose not to have
         *  condition like this or verifying Bundle data
         */
        /*if (viewModel.latestForcecastTrams.value != null) {
            viewModel.displayForecast()
        }*/

        viewModel.fetchForecast()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /**
         * TODO: Any important info that should survive configuration change or android killing app
         * due to memory constraints. This app being the one that deals with real time info, I see
         * no point is saving old data here. It does not even serve the purpose of a good user experience
         */
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forecast_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            viewModel.refresh()
        }
        return super.onOptionsItemSelected(item)
    }
}