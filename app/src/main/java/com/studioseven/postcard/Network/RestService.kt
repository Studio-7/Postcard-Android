package com.studioseven.postcard.Network

import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

interface RestService {

    @POST("user/signup")
    fun signUp(@Query("username") username: String,
               @Query("password") password: String,
               @Query("fname") fname: String,
               @Query("lname") lname: String,
               @Query("email") email: String): Observable<Map<String, String>>
}