package ie.koala.weather.model

data class WeatherSys(
        var message: String? = null,
        var id: String? = null,
        var sunset: String? = null,
        var sunrise: String? = null,
        var type: String? = null,
        var country: String? = null
)
