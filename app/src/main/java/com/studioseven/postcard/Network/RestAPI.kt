package com.studioseven.postcard.Network

import com.studioseven.postcard.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestAPI {
    companion object {
        var adapter = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getAppService(): RestService {
            return adapter.create(RestService::class.java)
        }
    }
}