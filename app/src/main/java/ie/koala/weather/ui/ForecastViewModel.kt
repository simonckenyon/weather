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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ie.koala.weather.data.ForecastRepository
import ie.koala.weather.model.Forecast
import ie.koala.weather.model.ForecastRequest
import ie.koala.weather.model.ForecastResult

/**
 * ViewModel for the [WeatherActivity] screen.
 * The ViewModel works with the [ForecastRepository] to get the data.
 */
class ForecastViewModel(private val repository: ForecastRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val queryLiveData = MutableLiveData<ForecastRequest>()
    private val forecastResult: LiveData<ForecastResult> = Transformations.map(queryLiveData) { forecastRequest: ForecastRequest ->
        repository.get(forecastRequest)
    }

    val forecast: LiveData<Forecast> = Transformations.switchMap(forecastResult) { forecastResult: ForecastResult ->
        forecastResult.data
    }
    val networkErrors: LiveData<String> = Transformations.switchMap(forecastResult) { forecastResult: ForecastResult ->
        forecastResult.networkErrors
    }

    /**
     * Search a repository based on a query string.
     */
    fun getForecast(request: ForecastRequest) {
        queryLiveData.postValue(request)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val request = lastQueryValue()
            if (request != null) {
                repository.requestMore(request)
            }
        }
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): ForecastRequest? = queryLiveData.value

    override fun toString(): String {
        return "ForecastViewModel(" +
                "repository=$repository," +
                "queryLiveData=$queryLiveData," +
                "forecastResult=$forecastResult," +
                "forecast=$forecast," +
                "networkErrors=$networkErrors" +
                ")"
    }

}