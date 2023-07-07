package com.example.todoapp.di.modules

import android.content.Context
import com.example.todoapp.data.dataSource.network.NetworkSource
import com.example.todoapp.data.dataSource.network.api.RetrofitService
import com.example.todoapp.utils.Constants.SERVER_URL
import com.example.todoapp.utils.Constants.TIMEOUT
import com.example.todoapp.utils.SharedPreferencesHelper
import com.example.todoapp.utils.internetConnection.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitClient(
        client: OkHttpClient
    ): Retrofit {
        System.setProperty("http.keepAlive", "false")
        return Retrofit.Builder()
            .client(client)
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(interceptor).build()

    @Provides
    fun getInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Reusable
    fun provideNetworkSource(
        sharedPreferencesHelper: SharedPreferencesHelper,
        retrofitService: RetrofitService
    ): NetworkSource = NetworkSource(sharedPreferencesHelper, retrofitService)

    @Provides
    @Singleton
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)

}
