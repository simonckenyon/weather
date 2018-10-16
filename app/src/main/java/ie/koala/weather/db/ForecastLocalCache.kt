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

package ie.koala.weather.db

import androidx.lifecycle.LiveData
import ie.koala.weather.model.Forecast
import ie.koala.weather.model.ForecastRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class ForecastLocalCache(
        private val forecastDao: ForecastDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a forecast into the database, on a background thread.
     */
    fun insert(forecast: Forecast, insertFinished: ()-> Unit) {
        ioExecutor.execute {
            log.debug("insert.execute: inserting  forecast=${forecast}")
            forecastDao.insert(forecast)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<Forecast> from the Dao, based on a lat and lon.
     * @param name city name
     */
    fun forecastByLatAndLon(request: ForecastRequest): LiveData<Forecast> {
        return forecastDao.forecastByRequest(request.lat, request.lon)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(ForecastLocalCache::class.java)
    }
}