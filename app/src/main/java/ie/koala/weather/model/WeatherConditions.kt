package ie.koala.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class WeatherConditions(
        @field:Json(name = "clouds") var clouds: Clouds = Clouds(),
        @field:Json(name = "dt") var dt: String = "",
        @field:Json(name = "wind") var wind: Wind = Wind(),
        @field:Json(name = "sys") var sys: Sys = Sys(),
        @field:Json(name = "weather") var weather: List<Weather> = listOf(),
        @field:Json(name = "dt_txt") var dt_txt: String = "",
        @field:Json(name = "rain") var rain: Rain = Rain(),
        @field:Json(name = "snow") var snow: Snow = Snow(),
        @field:Json(name = "main") var main: Main = Main()
){
    @PrimaryKey(autoGenerate = true)
    var listId: Int = 0
}

