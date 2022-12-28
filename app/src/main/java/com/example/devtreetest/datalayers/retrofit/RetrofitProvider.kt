package com.example.devtreetest.datalayers.retrofit


import com.example.devtreetest.core.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitProvider {


    companion object {
        private var retrofit: Retrofit? = null
        private var okHttpClient: OkHttpClient? = null

        private val loggingInterceptor = HttpLoggingInterceptor { }.setLevel(HttpLoggingInterceptor.Level.BODY)


        private fun getHttpClient(): OkHttpClient? {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(loggingInterceptor)
                        .build()
            }
            return okHttpClient

        }

        fun getRetrofit(): Retrofit? {
            retrofit = retrofit ?: run {
                getHttpClient()?.let {
                    Retrofit.Builder()
                        .baseUrl(Constant.MAPS_API_URL)
                        .client(it)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return retrofit
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofit()?.create(serviceClass)!!
        }
    }
}
