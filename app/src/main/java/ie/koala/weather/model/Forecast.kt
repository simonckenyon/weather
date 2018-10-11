package ie.koala.weather.model

data class Forecast(
        var message: String? = null,
        var cnt: String? = null,
        var cod: String? = null,
        var list: Array<DailyForecast>? = null,
        var city: City? = null
)
