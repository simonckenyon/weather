package ie.koala.weather.model

data class WeatherMain(
        var humidity: String? = null,
        var pressure: String? = null,
        var temp_max: String? = null,
        var temp_min: String? = null,
        var temp: String? = null
)
