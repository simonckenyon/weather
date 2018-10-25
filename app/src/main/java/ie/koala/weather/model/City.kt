package ie.koala.weather.model

import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City(
        @field:Json(name = "id") var id: Int = -1,
        @field:Json(name = "name") var name: String = "",
        @field:Json(name = "coord") @Embedded var coord: Coord = Coord(0.0f, 0.0f),
        @field:Json(name = "country") var country: String = "",
        @field:Json(name = "population") var population: Int = 0,
        @field:Json(name = "sys") @Embedded var sys: Sys? = Sys()
)
