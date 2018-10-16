package ie.koala.weather.model

import com.squareup.moshi.Json

data class Wind(
        @field:Json(name = "speed") var speed: String = "",
        @field:Json(name = "deg") var deg: String = ""
)
