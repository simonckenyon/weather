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

package ie.koala.weather.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ie.koala.weather.api.ForecastService
import ie.koala.weather.api.getForecast
import ie.koala.weather.db.ForecastLocalCache
import ie.koala.weather.model.Forecast
import ie.koala.weather.model.ForecastRequest
import ie.koala.weather.model.ForecastResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Repository class that works with local and remote data sources.
 */
class ForecastRepository(
        private val service: ForecastService,
        private val cache: ForecastLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    fun get(request: ForecastRequest): ForecastResult {
        log.debug("get: request=$request")
        lastRequestedPage = 1
        requestAndSaveData(request)

        // Get data from the local cache
        val data = cache.forecastByLatAndLon(request)

        return ForecastResult(data, networkErrors)
    }

    fun requestMore(request: ForecastRequest) {
        requestAndSaveData(request)
    }

    private fun requestAndSaveData(request: ForecastRequest) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        getForecast(service, request, { forecast: Forecast ->
            cache.insert(forecast) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }


    companion object {
        val log: Logger = LoggerFactory.getLogger(ForecastRepository::class.java)
    }
}