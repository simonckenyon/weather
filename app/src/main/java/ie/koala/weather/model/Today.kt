package ie.koala.weather.model

data class Today(
        var id: String? = null,
        var dt: String? = null,
        var clouds: Clouds? = null,
        var coord: Coord? = null,
        var wind: Wind? = null,
        var cod: String? = null,
        var visibility: String? = null,
        var sys: WeatherSys? = null,
        var name: String? = null,
        var base: String? = null,
        var weather: Array<Weather>? = null,
        var main: WeatherMain? = null
)
