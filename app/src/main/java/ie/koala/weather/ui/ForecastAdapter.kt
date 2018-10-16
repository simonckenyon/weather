package ie.koala.weather.ui

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ie.koala.weather.R
import ie.koala.weather.model.Weather
import ie.koala.weather.model.WeatherConditions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ForecastAdapter(ctx: Context,
                      textViewResourceId: Int,
                      private val weatherConditionsList: List<WeatherConditions>): ArrayAdapter<WeatherConditions>(ctx, textViewResourceId) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        view = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.list_forecast, parent, false)
        } else {
            convertView
        }

        val highTempText = view.findViewById(R.id.highTempText) as TextView
        val lowTempText = view.findViewById(R.id.lowTempText) as TextView
        val weatherText = view.findViewById(R.id.weatherText) as TextView
        val dayOfWeekText = view.findViewById(R.id.dayOfWeekText) as TextView

        val weatherFont = Typeface.createFromAsset(context.assets, "fonts/Weather-Fonts.ttf")
        val listIconText = view.findViewById(R.id.listIconText) as TextView
        listIconText.typeface = weatherFont

        val weatherConditions: WeatherConditions = weatherConditionsList[position + 1]

        highTempText.text = weatherConditions.main?.temp_max
        lowTempText.text = weatherConditions.main?.temp_min

        val weatherList: List<Weather>? = weatherConditions.weather
        if (weatherList != null && weatherList.isNotEmpty()) {
            val weather = weatherList[0]
            val weatherId = weather.id?.toInt() ?: 0
            setWeatherIcon(weatherId, listIconText)
            weatherText.text = weather.description
        }

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, position + 1)
        val date = calendar.time
        dayOfWeekText.text = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(date.time)

        return view
    }

    private fun setWeatherIcon(actualId: Int, listIconText: TextView) {
        val id = actualId / 100
        var icon = ""
        if (actualId == 800) {
            icon = context.getString(R.string.weather_sunny)
        } else {
            when (id) {
                1 -> icon = context.getString(R.string.weather_sunny)
                2 -> icon = context.getString(R.string.weather_thunder)
                3 -> icon = context.getString(R.string.weather_drizzle)
                7 -> icon = context.getString(R.string.weather_foggy)
                8 -> icon = context.getString(R.string.weather_cloudy)
                6 -> icon = context.getString(R.string.weather_snowy)
                5 -> icon = context.getString(R.string.weather_rainy)
            }
        }
        listIconText.text = icon
    }
}
