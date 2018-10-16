package ie.koala.weather.model

import com.squareup.moshi.Json

data class Snow(
        @field:Json(name = "3h") var threeH: String = ""
)
