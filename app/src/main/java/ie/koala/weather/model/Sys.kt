package ie.koala.weather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sys(
        @field:Json(name = "pod") var pod: String = ""
)
