package ie.koala.weather.api

import ie.koala.weather.model.Forecast
import ie.koala.weather.model.Today
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetWeatherService {
    @GET("weather")
    fun getToday(@Header("x-api-key") key: String,
                 @Query("lat") latitude: String,
                 @Query("lon") longitude: String,
                 @Query("units") units: String): Call<Today>

    @GET("forecast")
    fun getForecast(@Header("x-api-key") key: String,
                    @Query("lat") latitude: String,
                    @Query("lon") longitude: String,
                    @Query("units") units: String,
                    @Query("cnt") numberOfDays: Int): Call<Forecast>
}