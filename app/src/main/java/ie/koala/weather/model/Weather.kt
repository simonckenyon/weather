package ie.koala.weather.model

import com.squareup.moshi.Json

data class Weather(
        @field:Json(name = "id") var id: String = "",
        @field:Json(name = "icon") var icon: String = "",
        @field:Json(name = "description") var description: String = "",
        @field:Json(name = "main") var main: String = ""
)
