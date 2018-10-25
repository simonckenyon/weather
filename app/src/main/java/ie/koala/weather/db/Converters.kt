package ie.koala.weather.db

import androidx.room.TypeConverter
import ie.koala.weather.model.WeatherConditions
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Type

object Converters {
    val log: Logger = LoggerFactory.getLogger(Converters::class.java)

    @TypeConverter
    @JvmStatic
    fun fromJson(json: String): List<WeatherConditions> {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, WeatherConditions::class.java)
        val jsonAdapter = moshi.adapter<List<WeatherConditions>>(type)

        try {
            val list = jsonAdapter.fromJson(json)
            if (list != null) {
                return list
            } else {
                return emptyList()
            }
        } catch (e: Exception) {
            log.error("fromJson: exception ", e)
        }
        return emptyList()


    }

    @TypeConverter
    @JvmStatic
    fun toJson(list: List<WeatherConditions>): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, WeatherConditions::class.java)
        val jsonAdapter = moshi.adapter<List<WeatherConditions>>(type)

        try {
            val json = jsonAdapter.toJson(list)
            return json
        } catch (e: Exception) {
            log.error("toJson: ", e)
        }
        return ""
    }
}