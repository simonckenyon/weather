package ie.koala.weather.model

import com.squareup.moshi.Json

data class Rain(
        @field:Json(name = "3h") var threeH: String = ""
)
