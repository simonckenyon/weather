package ie.koala.weather.model

import com.squareup.moshi.Json

data class Main(
        @field:Json(name = "temp_kf") var temp_kf: String = "",
        @field:Json(name = "humidity") var humidity: String = "",
        @field:Json(name = "pressure") var pressure: String = "",
        @field:Json(name = "temp_max") var temp_max: String = "",
        @field:Json(name = "sea_level") var sea_level: String = "",
        @field:Json(name = "temp_min") var temp_min: String = "",
        @field:Json(name = "temp") var temp: String = "",
        @field:Json(name = "grnd_level") var grnd_level: String = ""
)
