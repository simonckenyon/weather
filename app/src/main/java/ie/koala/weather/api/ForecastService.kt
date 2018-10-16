package ie.koala.weather.api

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
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

fun getForecast(
        service: ForecastService,
        request: ForecastRequest,
        onSuccess: (forecast: Forecast) -> Unit,
        onError: (error: String) -> Unit) {
    val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)

    log.debug("getForecast: request=$request")

    service.getForecastFromOpenWeatherMap(request.key, request.lat, request.lon, request.units).enqueue(
            object : Callback<ForecastResponse> {
                override fun onFailure(call: Call<ForecastResponse>?, t: Throwable) {
                    log.debug("getForecastFromOpenWeatherMap.onFailure: fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<ForecastResponse>?,
                        response: Response<ForecastResponse>
                ) {
                    log.debug("getForecastFromOpenWeatherMap.onResponse: got a response $response")
                    if (response.isSuccessful) {
                        val forecast = response.body()?.forecast ?: Forecast()
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
                                       @Query("units") units: String): Call<ForecastResponse>

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

        fun create(): ForecastService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    //.addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(ForecastService::class.java)
        }
    }
}