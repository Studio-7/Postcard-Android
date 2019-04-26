package com.studioseven.postcard.Network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RestService {

    @FormUrlEncoded
    @POST("user/signup")
    fun signUp(@Field("username") username: String,
               @Field("password") password: String,
               @Field("fname") fname: String,
               @Field("lname") lname: String,
               @Field("googleauth") googleauth: String,
               @Field("email") email: String): Call<Map<String, String>>

    @FormUrlEncoded
    @POST("user/login")
    fun signIn(@Field("username") username: String,
               @Field("googleauth") googleauth: String,
               @Field("password") password: String): Call<Map<String, String>>

    @POST("post/createtravelcapsule")
    @FormUrlEncoded
    fun createCapsule(@Field("token") token : String,
                      @Field("username") username: String,
                      @Field("title") title: String): Call<Map<String, String>>

    @POST("/post/createpost")
    @Multipart
    fun postMedia(@Part("token") token : RequestBody,
                  @Part("username") username: RequestBody,
                  @Part("title") title: RequestBody,
                  @Part("message") message: RequestBody,
                  @Part file: MultipartBody.Part,
                  @Part("travelcapsule") id: RequestBody): Call<Map<String, String>>

    @FormUrlEncoded
    @POST("/search/find")
    fun search(@Field("username") username: String,
               @Field("token") token: String,
               @Field("query") query: String): Call<Map<String, Any>>

    @FormUrlEncoded
    @POST("/feed/main")
    fun getFeed(@Field("username") username: String,
                @Field("token") token: String): Call<Map<String, Any>>

    @FormUrlEncoded
    @POST("/post/getpost")
    fun getPosts(@Field("username") username: String,
                 @Field("token") token: String,
                 @Field("ids") ids: String): Call<Map<String, Any>>
}

