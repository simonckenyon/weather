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

package ie.koala.weather.ui

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.koala.weather.R
import ie.koala.weather.model.Weather
import ie.koala.weather.model.WeatherConditions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * View Holder for a [WeatherConditions] RecyclerView list item.
 */
class WeatherConditionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val highTempText = view.findViewById(R.id.highTempText) as TextView
    private val lowTempText = view.findViewById(R.id.lowTempText) as TextView
    private val weatherText = view.findViewById(R.id.weatherText) as TextView
    private val dayOfWeekText = view.findViewById(R.id.dayOfWeekText) as TextView
    private val listIconText = view.findViewById(R.id.listIconText) as TextView

    val weatherFont = Typeface.createFromAsset(view.context.assets, "fonts/Weather-Fonts.ttf")

    private var weatherConditions: WeatherConditions? = null

    fun bind(weatherConditions: WeatherConditions?) {
        if (weatherConditions == null) {
            log.debug("bind: weatherConditions is empty?")
        } else {
            showWeatherConditions(itemView.context, weatherConditions)
        }
    }

    private fun showWeatherConditions(context: Context, weatherConditions: WeatherConditions) {
        this.weatherConditions = weatherConditions

        listIconText.typeface = weatherFont

        highTempText.text = weatherConditions.main.temp_max
        lowTempText.text = weatherConditions.main.temp_min

        val weatherList: List<Weather>? = weatherConditions.weather
        if (weatherList != null && weatherList.isNotEmpty()) {
            val weather = weatherList[0]
            val weatherId = weather.id.toInt()
            setWeatherIcon(context, weatherId, listIconText)
            weatherText.text = weather.description
        }

        try {
            val seconds = weatherConditions.dt.toLong()
            val date = Date(seconds * 1000)
            dayOfWeekText.text = SimpleDateFormat("EEE, MMM d h a", Locale.getDefault()).format(date.time)
        } catch (e: NumberFormatException) {
            log.error("showWeatherConditions: exception ", e)
            dayOfWeekText.text = context.getString(R.string.message_unknown_date)
        }
    }

    private fun setWeatherIcon(context: Context, actualId: Int, listIconText: TextView) {
        val id = actualId / 100
        var icon = ""
        if (actualId == 800) {
            icon = context.getString(R.string.weather_sunny)
        } else {
            when (id) {
                1 -> icon = context.getString(R.string.weather_sunny)
                2 -> icon = context.getString(R.string.weather_thunder)
                3 -> icon = context.getString(R.string.weather_drizzle)
                7 -> icon = context.getString(R.string.weather_foggy)
                8 -> icon = context.getString(R.string.weather_cloudy)
                6 -> icon = context.getString(R.string.weather_snowy)
                5 -> icon = context.getString(R.string.weather_rainy)
            }
        }
        listIconText.text = icon
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(WeatherConditionsViewHolder::class.java)

        fun create(parent: ViewGroup): WeatherConditionsViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_weather_conditions, parent, false)
            return WeatherConditionsViewHolder(view)
        }
    }
}