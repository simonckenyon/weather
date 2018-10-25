package ie.koala.weather.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wind(
        @field:Json(name = "speed") var speed: String = "",
        @field:Json(name = "deg") var deg: String = ""
)
