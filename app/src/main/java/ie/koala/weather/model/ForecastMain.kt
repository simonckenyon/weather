package ie.koala.weather.model

data class ForecastMain(
        var temp_kf: String? = null,
        var humidity: String? = null,
        var pressure: String? = null,
        var temp_max: String? = null,
        var sea_level: String? = null,
        var temp_min: String? = null,
        var temp: String? = null,
        var grnd_level: String? = null
)
