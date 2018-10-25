package ie.koala.weather.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Forecast(
        @field:Json(name = "message") var message: String = "",
        @field:Json(name = "cnt") var cnt: String = "",
        @field:Json(name = "cod") var cod: String = "",
        @field:Json(name = "list") var list: List<WeatherConditions> = listOf(),
        @field:Json(name = "city") @Embedded var city: City = City()
){
    @PrimaryKey(autoGenerate = true)
    var forecastId: Int = 0
}
