package ie.koala.weather

//import android.graphics.Typeface
//import android.location.Location
//import android.location.LocationListener
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.appcompat.app.AppCompatActivity
//import ie.koala.weather.api.ForecastService
//import ie.koala.weather.api.RetrofitInstance
//import ie.koala.weather.location.LocationUtils
//import ie.koala.weather.model.Forecast
//import ie.koala.weather.model.TodaysForecast
//import ie.koala.weather.ui.ForecastAdapter
//import ie.koala.weather.ui.ListViewUtils
//import ie.koala.weather.ui.snackbar
//import ie.koala.weather.viewmodel.ForecastItem
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.layout_today.*
//import org.jetbrains.anko.doAsync
//import org.jetbrains.anko.uiThread
//import org.json.JSONException
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.math.BigDecimal
//import java.util.*
//
//class OldMainActivity : AppCompatActivity(), LocationListener {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // set up the view
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
//        toolbar.title = getString(R.string.app_name)
//        listView.isEnabled = false  // so you can't select the entries
//        weatherIcon.typeface = Typeface.createFromAsset(assets, "fonts/Weather-Fonts.ttf")
//
//        // get the current location
//        LocationUtils.requestPermission(this, coordinator_layout_main)
//    }
//
//    public override fun onPause() {
//        super.onPause()
//        LocationUtils.removeLocationUpdates(this)
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        requestLocation()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.action_refresh -> consume {
//            requestLocation()
//        }
//        else -> super.onOptionsItemSelected(item)
//    }
//
//    private inline fun consume(f: () -> Unit): Boolean {
//        f()
//        return true
//    }
//
//    private fun requestLocation() {
//        LocationUtils.requestLocationUpdates(this, TEN_MINUTES, ONE_MILE_IN_METRES)
//    }
//
//    private fun getCurrentWeather(lat: String, lon: String) {
//        val key = getString(R.string.open_weather_maps_app_id)
//        doAsync {
//            val service = RetrofitInstance.retrofitInstance.create(ForecastService::class.java)
//            val call: Call<TodaysForecast> = service.getToday(key, lat, lon, UNITS)
//            call.enqueue(object : Callback<TodaysForecast> {
//                override fun onResponse(call: Call<TodaysForecast>, response: Response<TodaysForecast>) {
//                    val today = response.body()
//                    if (today != null) {
//                        try {
//                            val weather = today.weather?.get(0)
//                            val iconId = weather?.id?.toInt()
//                            val description = weather?.description
//                            val main = today.main
//                            val temperature = BigDecimal(main?.temp)
//                            val tempMax = BigDecimal(main?.temp_max)
//                            val tempMin = BigDecimal(main?.temp_min)
//                            val humidityPercentage = main?.humidity?.toInt()
//                            val location = today.name
//                            val sys = today.sys
//                            val sunrise = sys?.sunrise?.toLong()
//                            val sunset = sys?.sunset?.toLong()
//
//                            uiThread {
//                                name.text = location
//                                weatherIcon.text = description
//                                temp.text = getString(R.string.temperature, temperature)
//                                tempHigh.text = getString(R.string.temperature_max, tempMax)
//                                tempLow.text = getString(R.string.temperature_min, tempMin)
//                                humidity.text = getString(R.string.humidity, humidityPercentage)
//                                if (iconId != null && sunrise != null && sunset != null) {
//                                    setWeatherIcon(iconId, sunrise * 1000, sunset * 1000)
//                                }
//                            }
//                        } catch (e: JSONException) {
//                            log.error("renderCurrentWeather: exception ", e)
//                            uiThread {
//                                coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                            }
//                        }
//                    } else {
//                        log.error("renderCurrentWeather: today is null")
//                        uiThread {
//                            coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<TodaysForecast>, t: Throwable) {
//                    log.error("getCurrentWeather.onFailure: exception", t)
//                    coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                }
//            })
//        }
//    }
//
//    private fun getFiveDayForecast(lat: String, lon: String) {
//        val key = getString(R.string.open_weather_maps_app_id)
//        doAsync {
//            val service = RetrofitInstance.retrofitInstance.create(ForecastService::class.java)
//            val call: Call<Forecast> = service.getForecastFromOpenWeatherMap(key, lat, lon, UNITS, NUMBER_OF_DAYS)
//            call.enqueue(object : Callback<Forecast> {
//                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
//                    val forecast = response.body()
//                    if (forecast != null) {
//                        try {
//                            val forecastItemList: MutableList<ForecastItem> = ArrayList()
//                            val list = forecast.list
//                            if (list != null) {
//                                for (dailyForecast in list) {
//                                    val forecastItem = ForecastItem()
//
//                                    val main = dailyForecast.main
//                                    val tempMax = BigDecimal(main?.temp_max)
//                                    forecastItem.highTemp = getString(R.string.temperature_max, tempMax)
//                                    val tempMin = BigDecimal(main?.temp_min)
//                                    forecastItem.lowTemp = getString(R.string.temperature_max, tempMin)
//
//                                    val weather = dailyForecast.weather?.get(0)
//                                    forecastItem.weather = weather?.description
//                                    forecastItem.weatherId = weather?.id
//
//                                    forecastItemList.add(forecastItem)
//                                }
//                            } else {
//                                // no daily forecasts
//                                log.error("getFiveDayForecast: dailyForecast list is null")
//                                uiThread {
//                                    coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                                }
//                            }
//                            uiThread {
//                                val testAdapter = ForecastAdapter(this@OldMainActivity, 0, forecastItemList)
//                                listView.adapter = testAdapter
//                                ListViewUtils.setListViewHeight(listView)
//                            }
//                        } catch (e: JSONException) {
//                            log.error("getFiveDayForecast: exception ", e.toString())
//                            uiThread {
//                                coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                            }
//                        }
//                    } else {
//                        log.error("getFiveDayForecast: forecast is null")
//                        uiThread {
//                            coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<Forecast>, t: Throwable) {
//                    log.error("getFiveDayForecast.onFailure: exception", t)
//                    coordinator_layout_main.snackbar(getString(R.string.message_no_forecast))
//                }
//            })
//        }
//    }
//
//    private fun setWeatherIcon(actualId: Int, sunrise: Long, sunset: Long) {
//        val id = actualId / 100
//        var icon = ""
//        if (actualId == 800) {
//            val currentTime = Date().time
//            icon = if (currentTime in sunrise..(sunset - 1)) {
//                getString(R.string.weather_sunny)
//            } else {
//                getString(R.string.weather_clear_night)
//            }
//        } else {
//            when (id) {
//                2 -> icon = getString(R.string.weather_thunder)
//                3 -> icon = getString(R.string.weather_drizzle)
//                7 -> icon = getString(R.string.weather_foggy)
//                8 -> icon = getString(R.string.weather_cloudy)
//                6 -> icon = getString(R.string.weather_snowy)
//                5 -> icon = getString(R.string.weather_rainy)
//            }
//        }
//        weatherIcon.text = icon
//    }
//
//    override fun onLocationChanged(location: Location?) {
//        if (location != null) {
//            getCurrentWeather(location.latitude.toString() + "", location.longitude.toString() + "")
//            getFiveDayForecast(location.latitude.toString() + "", location.longitude.toString() + "")
//        }
//    }
//
//    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
//    }
//    override fun onProviderEnabled(provider: String) {
//    }
//    override fun onProviderDisabled(provider: String) {
//    }
//
//    companion object {
//        val log: Logger = LoggerFactory.getLogger(OldMainActivity::class.java)
//
//        const val TEN_MINUTES = (10 * 60 * 1000).toLong()
//        const val ONE_MILE_IN_METRES: Float = 1609.344.toFloat()
//
//        const val UNITS = "metric"      // or "imperial"
//        const val NUMBER_OF_DAYS = 7
//    }
//}
