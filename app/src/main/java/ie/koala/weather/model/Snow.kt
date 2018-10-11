package ie.koala.weather.model

import com.google.gson.annotations.SerializedName

data class Snow(
        @SerializedName("3h")
        var threeH: String? = null
)
