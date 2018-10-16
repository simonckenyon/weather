package ie.koala.weather.model

import com.squareup.moshi.Json

data class Coord(
        @field:Json(name = "lat") var lat: String = "0.0",
        @field:Json(name = "lon") var lon: String = "0.0"
)
