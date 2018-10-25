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

import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import ie.koala.weather.location.LocationUtils
import ie.koala.weather.model.Forecast
import ie.koala.weather.model.ForecastRequest
import ie.koala.weather.model.WeatherConditions
import ie.koala.weather.viewmodel.ForecastViewModel
import ie.koala.weather.ui.WeatherConditionsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_today.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var viewModel: ForecastViewModel
    private val adapter = WeatherConditionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)
        //setupScrollListener()

        // get the current location
        LocationUtils.requestPermission(this, coordinator_layout_main)

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(ForecastViewModel::class.java)

        initAdapter()
        val query: ForecastRequest? = savedInstanceState?.getParcelable(LAST_SEARCH_QUERY)
        if (query != null) {
            viewModel.getForecast(query)
            adapter.submitList(null)
        }
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

    private fun initAdapter() {
        list.adapter = adapter
        viewModel.forecast.observe(this, Observer<Forecast> {
            list.setEmptyView(emptyView)
            if (it != null) {
                val weatherConditionsList: List<WeatherConditions>? = it.list
                if (weatherConditionsList != null && weatherConditionsList.isNotEmpty()) {
                    val city = it.city.name
                    val today = weatherConditionsList.first()
                    displayToday(city, today)
                    val remaining: List<WeatherConditions> = weatherConditionsList.drop(1)
                    adapter.submitList(remaining)
                } else {
                    log.warn("initAdapter: weatherConditionsList is null or empty")
                }
            } else {
                log.warn("initAdapter: forecast is null")
            }
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun displayToday(city: String, today: WeatherConditions) {
        val weather = today.weather[0]
        val description = weather.description
        val main = today.main
        val temperature = BigDecimal(main.temp)
        val tempMax = BigDecimal(main.temp_max)
        val tempMin = BigDecimal(main.temp_min)
        val humidityPercentage = main.humidity.toInt()

        val weatherFont = Typeface.createFromAsset(assets, "fonts/Weather-Fonts.ttf")
        weatherIcon.typeface = weatherFont
        weatherIcon.text = description

        name.text = city
        temp.text = getString(R.string.temperature, temperature)
        tempHigh.text = getString(R.string.temperature_max, tempMax)
        tempLow.text = getString(R.string.temperature_min, tempMin)
        humidity.text = getString(R.string.humidity, humidityPercentage)

        val iconId = weather.id.toInt()
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

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            val key = getString(R.string.open_weather_maps_app_id)
            viewModel.getForecast(ForecastRequest(key, location.latitude.toString(), location.longitude.toString(), UNITS))
            adapter.submitList(null)
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

        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}
