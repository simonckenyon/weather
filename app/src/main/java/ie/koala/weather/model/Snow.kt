package ie.koala.weather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Snow(
        @field:Json(name = "3h") var threeH: String = ""
)
