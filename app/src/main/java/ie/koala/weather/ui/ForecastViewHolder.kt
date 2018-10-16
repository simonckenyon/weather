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

//import android.content.Intent
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import ie.koala.weather.R
//import ie.koala.weather.model.Forecast
//
///**
// * View Holder for a [Forecast] RecyclerView list item.
// */
//class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//    private val name: TextView = view.findViewById(R.id.repo_name)
//    private val description: TextView = view.findViewById(R.id.repo_description)
//    private val stars: TextView = view.findViewById(R.id.repo_stars)
//    private val language: TextView = view.findViewById(R.id.repo_language)
//    private val forks: TextView = view.findViewById(R.id.repo_forks)
//
//    private var forecast: Forecast? = null
//
//    init {
//        view.setOnClickListener {
//            forecast?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
//        }
//    }
//
//    fun bind(forecast: Forecast?) {
//        if (forecast == null) {
//            val resources = itemView.resources
//            name.text = resources.getString(R.string.loading)
//            description.visibility = View.GONE
//            language.visibility = View.GONE
//            stars.text = resources.getString(R.string.unknown)
//            forks.text = resources.getString(R.string.unknown)
//        } else {
//            showRepoData(Forecast)
//        }
//    }
//
//    private fun showRepoData(forecast: Forecast) {
//        this.forecast = forecast
//        name.text = forecast.fullName
//
//        // if the description is missing, hide the TextView
//        var descriptionVisibility = View.GONE
//        if (forecast.description != null) {
//            description.text = forecast.description
//            descriptionVisibility = View.VISIBLE
//        }
//        description.visibility = descriptionVisibility
//
//        stars.text = forecast.stars.toString()
//        forks.text = forecast.forks.toString()
//
//        // if the language is missing, hide the label and the value
//        var languageVisibility = View.GONE
//        if (!forecast.language.isNullOrEmpty()) {
//            val resources = this.itemView.context.resources
//            language.text = resources.getString(R.string.language, forecast.language)
//            languageVisibility = View.VISIBLE
//        }
//        language.visibility = languageVisibility
//    }
//
//    companion object {
//        fun create(parent: ViewGroup): ForecastViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.repo_view_item, parent, false)
//            return ForecastViewHolder(view)
//        }
//    }
//}