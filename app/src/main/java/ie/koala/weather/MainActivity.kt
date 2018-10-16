/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.koala.weather

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.koala.weather.location.LocationUtils
import ie.koala.weather.model.Forecast
import ie.koala.weather.model.ForecastRequest
import ie.koala.weather.model.WeatherConditions
import ie.koala.weather.ui.ForecastViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_today.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var viewModel: ForecastViewModel
    //private val adapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)
        setupScrollListener()

        // get the current location
        LocationUtils.requestPermission(this, coordinator_layout_main)

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(ForecastViewModel::class.java)

        initAdapter()
        log.debug("onCreate: viewModel=$viewModel")
        val query: ForecastRequest = savedInstanceState?.getParcelable<ForecastRequest>(LAST_SEARCH_QUERY)
                ?: defaultSearch()
        viewModel.getForecast(query)
        initSearch(query)
    }

    public override fun onPause() {
        super.onPause()
        LocationUtils.removeLocationUpdates(this)
    }

    public override fun onResume() {
        super.onResume()
        requestLocation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        log.debug("onSaveInstanceState: viewModel=$viewModel")
        outState.putParcelable(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_refresh -> consume {
            requestLocation()
        }
        else -> super.onOptionsItemSelected(item)
    }

    private inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun requestLocation() {
        LocationUtils.requestLocationUpdates(this, TEN_MINUTES, ONE_MILE_IN_METRES)
    }

    private fun defaultSearch(): ForecastRequest {
        val key = getString(R.string.open_weather_maps_app_id)
        return ForecastRequest(key, lat, lon, UNITS)
    }

    private fun initAdapter() {
        //list.adapter = adapter
        log.debug("initAdapter: viewModel=$viewModel")
        log.debug("initAdapter: viewModel.forecast=${viewModel.forecast}")
        viewModel.forecast.observe(this, Observer<Forecast> { forecast: Forecast ->
            log.debug("initAdapter: forecast=$forecast")
            if (forecast.list.isNotEmpty()) {
                // get weather from forecast
                //log.debug("initAdapter: viewModel=$viewModel")

                val city = forecast.city?.name ?: "Unknown"
                val weatherConditionsList: List<WeatherConditions>? = forecast.list
                if (weatherConditionsList != null && weatherConditionsList.isNotEmpty()) {
                    showEmptyList(false)
                    log.debug("initAdapter: weatherConditionsList.size=${weatherConditionsList.size}")
                    val today = weatherConditionsList.first()
                    displayToday(city, today)
                    //val remaining = weatherConditionsList.drop(1)
                    //adapter.submitList(remaining)
                } else {
                    log.debug("initAdapter: weatherConditionsList is null or empty")
                    showEmptyList(true)
                }
            } else {
                log.debug("initAdapter: forecast is null")
                showEmptyList(true)
            }
        })
        log.debug("initAdapter: after viewModel.forecast...")
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })
    }

    private fun displayToday(city: String, today: WeatherConditions) {
        val weather = today.weather?.get(0)
        val iconId = weather?.id?.toInt() ?: 800    // always an optimist
        val description = weather?.description
        val main = today.main
        val temperature = BigDecimal(main?.temp)
        val tempMax = BigDecimal(main?.temp_max)
        val tempMin = BigDecimal(main?.temp_min)
        val humidityPercentage = main?.humidity?.toInt()
        val location = city

        name.text = location
        weatherIcon.text = description
        temp.text = getString(R.string.temperature, temperature)
        tempHigh.text = getString(R.string.temperature_max, tempMax)
        tempLow.text = getString(R.string.temperature_min, tempMin)
        humidity.text = getString(R.string.humidity, humidityPercentage)
        setWeatherIcon(iconId)
    }

    private fun setWeatherIcon(actualId: Int) {
        var icon = ""
        if (actualId == 800) {
            icon = getString(R.string.weather_sunny)
        } else {
            when (actualId / 100) {
                2 -> icon = getString(R.string.weather_thunder)
                3 -> icon = getString(R.string.weather_drizzle)
                7 -> icon = getString(R.string.weather_foggy)
                8 -> icon = getString(R.string.weather_cloudy)
                6 -> icon = getString(R.string.weather_snowy)
                5 -> icon = getString(R.string.weather_rainy)
            }
        }
        weatherIcon.text = icon
    }

    private fun initSearch(query: ForecastRequest) {
//        locationText.setText(query)
//
//        locationText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_GO) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
//        locationText.setOnKeyListener { _, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
//    }

//    private fun updateRepoListFromInput() {
//        locationText.text?.trim().let { searchString ->
//            if (searchString != null && searchString.isNotEmpty()) {
//                list.scrollToPosition(0)
//                viewModel.getForecastFromOpenWeatherMap(searchString.toString())
//                adapter.submitList(null)
//            }
//        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }

    private fun setupScrollListener() {
        val layoutManager = list.layoutManager as LinearLayoutManager
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                log.debug("setupScrollListener: viewModel=$viewModel")
                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            lat = location.latitude.toString()
            lon = location.longitude.toString()
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)

        private const val TEN_MINUTES = (10 * 60 * 1000).toLong()
        private const val ONE_MILE_IN_METRES: Float = 1609.344.toFloat()

        private const val UNITS = "metric"      // or "imperial"
        private const val NUMBER_OF_DAYS = 7

        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"

        private var lat: String = "0.0"
        private var lon: String = "0.0"
    }
}
