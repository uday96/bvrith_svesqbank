package com.example.bvrith_svesqbank.api

import com.example.bvrith_svesqbank.data.Questions
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

    fun getQuestions(dept: String, subj: String, level: Int, callback: Callback<Questions>) {
        service.getQuestions(dept, subj, level).enqueue(callback)
    }
}