package ie.koala.weather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coord(
        @field:Json(name = "lat") var lat: Float = 0.0f,
        @field:Json(name = "lon") var lon: Float = 0.0f
)
