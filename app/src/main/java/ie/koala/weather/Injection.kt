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

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import ie.koala.weather.viewmodel.ViewModelFactory
import ie.koala.weather.data.ForecastRepository
import ie.koala.weather.api.ForecastService
import ie.koala.weather.db.ForecastDatabase
import ie.koala.weather.db.ForecastLocalCache
import java.util.concurrent.Executors

object Injection {

    /**
     * Creates an instance of [ForecastLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): ForecastLocalCache {
        val database = ForecastDatabase.getInstance(context)
        return ForecastLocalCache(database.weatherDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [ForecastRepository] based on the [ForecastService] and a
     * [ForecastLocalCache]
     */
    private fun provideForecastRepository(context: Context): ForecastRepository {
        return ForecastRepository(ForecastService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideForecastRepository(context))
    }

}