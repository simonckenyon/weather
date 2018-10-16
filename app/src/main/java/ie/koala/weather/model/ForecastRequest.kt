package ie.koala.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastRequest(
        val key: String,
        val lat: String,
        val lon: String,
        val units: String
) : Parcelable
