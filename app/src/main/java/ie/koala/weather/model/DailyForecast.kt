package ie.koala.weather.model

data class DailyForecast(
        var clouds: Clouds? = null,
        var dt: String? = null,
        var wind: Wind? = null,
        var sys: ForecastSys? = null,
        var weather: Array<Weather>? = null,
        var dt_txt: String? = null,
        var rain: Rain? = null,
        var snow: Snow? = null,
        var main: ForecastMain? = null
)
