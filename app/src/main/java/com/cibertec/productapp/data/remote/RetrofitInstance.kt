package com.cibertec.productapp.data.remote

import com.cibertec.productapp.data.remote.api.FakeStoreApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Instancia singleton de Retrofit para realizar llamadas a la API
 */
object RetrofitInstance {

    private const val BASE_URL = "https://fakestoreapi.com/"

    /**
     * Cliente HTTP con timeouts y logging
     */
    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Configuración de Retrofit con el convertidor Gson
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Instancia de la API FakeStore
     */
    val api: FakeStoreApi by lazy {
        retrofit.create(FakeStoreApi::class.java)
    }
}
