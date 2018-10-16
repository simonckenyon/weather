package ie.koala.weather.model

import com.squareup.moshi.Json

data class Clouds(
        @field:Json(name = "all") var all: String = ""
)
