package ie.koala.weather.model

data class City(
        var id: Int? = null,
        var name: String? = null,
        var coord: Coord? = null,
        var country: String? = null,
        var population: Int? = null,
        var sys: WeatherSys? = null
)
