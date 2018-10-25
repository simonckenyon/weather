package ie.koala.weather.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ie.koala.weather.MainActivity
import ie.koala.weather.model.Forecast
import ie.koala.weather.model.ForecastRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

fun getForecast(
        service: ForecastService,
        request: ForecastRequest,
        onSuccess: (forecast: Forecast) -> Unit,
        onError: (error: String) -> Unit) {
    val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)

    service.getForecastFromOpenWeatherMap(request.key, request.lat, request.lon, request.units).enqueue(
            object : Callback<Forecast> {
                override fun onFailure(call: Call<Forecast>?, t: Throwable) {
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    if (response.isSuccessful) {
                        val forecast = response.body()!!
                        onSuccess(forecast)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

interface ForecastService {
    @GET("forecast")
    fun getForecastFromOpenWeatherMap(@Header("x-api-key") key: String,
                                       @Query("lat") latitude: String,
                                       @Query("lon") longitude: String,
                                       @Query("units") units: String): Call<Forecast>

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

        fun create(): ForecastService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BODY   // was BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            val moshi = Moshi
                    .Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                    .create(ForecastService::class.java)
        }
    }
}