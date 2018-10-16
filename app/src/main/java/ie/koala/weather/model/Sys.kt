package ie.koala.weather.model

import com.squareup.moshi.Json

data class Sys(
        @field:Json(name = "pod") var pod: String = ""
)
