package ie.koala.weather.ui

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ie.koala.weather.R
import ie.koala.weather.viewmodel.ForecastItem

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ForecastAdapter(ctx: Context,
                      textViewResourceId: Int,
                      private val forecastItemList: List<ForecastItem>): ArrayAdapter<ForecastItem>(ctx, textViewResourceId) {

    // ignore the first entry as that is today and already displayed above this daily
    override fun getCount(): Int {
        return forecastItemList.size - 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        view = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.list_forecast, parent, false)
        } else {
            convertView
        }
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, position + 1)
        val date = calendar.time

        val maxTemp = view.findViewById(R.id.maxTemp) as TextView
        val lowTemp = view.findViewById(R.id.lowTemp) as TextView
        val weather1 = view.findViewById(R.id.weather1) as TextView
        val dayOfWeek = view.findViewById(R.id.dayOfWeek) as TextView

        val weatherFont = Typeface.createFromAsset(context.assets, "fonts/Weather-Fonts.ttf")
        val listIcon = view.findViewById(R.id.listIcon) as TextView
        listIcon.typeface = weatherFont

        val forecastItem: ForecastItem = forecastItemList[position + 1]

        maxTemp.text = forecastItem.highTemp
        lowTemp.text = forecastItem.lowTemp
        setWeatherIcon(Integer.valueOf(forecastItem.weatherId!!), listIcon)
        weather1.text = forecastItem.weather
        dayOfWeek.text = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(date.time)
        return view
    }

    private fun setWeatherIcon(actualId: Int, listIcon: TextView) {
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
        listIcon.text = icon
    }
}
