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

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ie.koala.weather.model.WeatherConditions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Adapter for the list of WeatherConditions.
 */
class WeatherConditionsAdapter : ListAdapter<WeatherConditions, RecyclerView.ViewHolder>(WeatherConditions_COMPARATOR) {

    /**
     * Submits a new list to be diffed, and displayed.
     *
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    override fun submitList(list: List<WeatherConditions>?) {
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WeatherConditionsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weatherConditionsItem = getItem(position)
        if (weatherConditionsItem != null) {
            (holder as WeatherConditionsViewHolder).bind(weatherConditionsItem)
        } else {
            log.debug("onBindViewHolder: weatherConditionsItem is null")
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(WeatherConditionsAdapter::class.java)

        private val WeatherConditions_COMPARATOR = object : DiffUtil.ItemCallback<WeatherConditions>() {
            override fun areItemsTheSame(oldItem: WeatherConditions, newItem: WeatherConditions): Boolean =
                    oldItem.dt == newItem.dt

            override fun areContentsTheSame(oldItem: WeatherConditions, newItem: WeatherConditions): Boolean =
                    oldItem == newItem
        }
    }
}