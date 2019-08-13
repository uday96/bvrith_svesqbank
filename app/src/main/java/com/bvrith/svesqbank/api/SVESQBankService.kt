package com.bvrith.svesqbank.api

import com.bvrith.svesqbank.data.Questions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SVESQBankService {

    @GET("/svesqbank/mobileLogin.jsp")
    fun login(@Query("uname") uname: String, @Query("pwd") pwd: String): Call<String>

    @GET("/svesqbank/getSubjects.jsp")
    fun getSubjects(@Query("dept") dept: String): Call<String>

    @GET("/svesqbank/getQuestions.jsp")
    fun getQuestions(@Query("dept") dept: String, @Query("subject") subj: String, @Query("unit") unit: String, @Query("level") level: Int): Call<Questions>

    @GET("/svesqbank/resultCapture.jsp")
    fun sendScore(@Query("uname") uname: String, @Query("subject") subj: String, @Query("score") score: Int): Call<String>

    @GET("/svesqbank/mobileRegister.jsp")
    fun signup(@Query("name") fname: String, @Query("rollnumber") rno: String, @Query("college") clg: String,
               @Query("role") role: String, @Query("mail") mail: String, @Query("mobile_number") mobile: String,
               @Query("uname") uname: String, @Query("password") pwd: String, @Query("dept") dept: String,
               @Query("section") sec: String): Call<String>

    @GET("/svesqbank/mobileResetPassword.jsp")
    fun resetPwd(@Query("uname") uname: String): Call<String>

    @GET("/svesqbank/mobilePasswordChange.jsp")
    fun forgotPwd(@Query("uname") uname: String, @Query("pwd") pwd: String): Call<String>

}
