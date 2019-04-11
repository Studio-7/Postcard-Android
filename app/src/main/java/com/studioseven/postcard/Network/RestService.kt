package com.studioseven.postcard.Network

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import java.util.concurrent.Callable

interface RestService {

    @FormUrlEncoded
    @POST("user/signup")
    fun signUp(@Field("username") username: String,
               @Field("password") password: String,
               @Field("fname") fname: String,
               @Field("lname") lname: String,
               @Field("email") email: String): Call<Map<String, String>>

    @POST("post/createtravelcapsule")
    @FormUrlEncoded
    fun createCapsule(@Field("token") token : String,
                      @Field("username") username: String,
                      @Field("title") title: String): Call<Map<String, String>>
}

