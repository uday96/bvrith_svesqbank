package com.bvrith.svesqbank.api

import com.bvrith.svesqbank.data.Questions
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class WebServiceUtil {

    private val service: SVESQBankService

    companion object {
        const val BASE_URL = "http://intranet.bvrithyderabad.edu.in:9000/"
    }

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit.create(SVESQBankService::class.java)
    }

    fun login(uname: String, pwd: String, callback: Callback<String>) {
        service.login(uname, pwd).enqueue(callback)
    }

    fun getSubjects(dept: String, callback: Callback<String>) {
        service.getSubjects(dept).enqueue(callback)
    }

    fun getQuestions(dept: String, subj: String, unit: String, level: Int, callback: Callback<Questions>) {
        service.getQuestions(dept, subj, unit, level).enqueue(callback)
    }

    fun sendScore(uname: String, subj: String, score: Int, callback: Callback<String>) {
        service.sendScore(uname, subj, score).enqueue(callback)
    }

    fun signup(name: String, rollnumber: String, college: String, role: String, mail: String, mobile_number: String, uname: String, password: String, dept: String, sec: String, callback: Callback<String>) {
        service.signup(name, rollnumber, college, role, mail, mobile_number, uname, password, dept, sec).enqueue(callback)
    }

    fun resetPwd(uname: String, callback: Callback<String>) {
        service.resetPwd(uname).enqueue(callback)
    }

    fun forgotPwd(uname: String, pwd: String, callback: Callback<String>) {
        service.forgotPwd(uname, pwd).enqueue(callback)
    }
}