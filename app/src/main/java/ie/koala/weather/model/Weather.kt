package ie.koala.weather.model

data class Weather(
        var id: String? = null,
        var icon: String? = null,
        var description: String? = null,
        var main: String? = null
)
