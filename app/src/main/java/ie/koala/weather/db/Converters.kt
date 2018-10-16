package ie.koala.weather.db

import androidx.room.TypeConverter
import ie.koala.weather.model.WeatherConditions

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromJson(value: String): List<WeatherConditions> {
        val listType = object : TypeToken<List<WeatherConditions>>() {

        }.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun toString(list: List<WeatherConditions>): String {
        val gson = Gson()
        return gson.toJson(list)
    }


}