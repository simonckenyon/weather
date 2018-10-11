package ie.koala.weather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {
        retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

    val retrofitInstance: Retrofit
        get() {
            return retrofit
        }
}