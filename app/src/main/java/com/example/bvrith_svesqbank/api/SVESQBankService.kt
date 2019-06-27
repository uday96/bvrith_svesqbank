package com.example.bvrith_svesqbank.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SVESQBankService {

    @GET("/svesqbank/mobileLogin.jsp")
    fun login(@Query("uname") uname: String, @Query("pwd") pwd: String): Call<String>

    @GET("/svesqbank/getSubjects.jsp")
    fun getSubjects(@Query("dept") dept: String): Call<String>

}
