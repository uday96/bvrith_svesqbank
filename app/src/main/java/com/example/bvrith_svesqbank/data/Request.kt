package com.example.bvrith_svesqbank.data

import android.util.Log
import java.net.URL

class Request(private val url: String) {

    fun run() : String {

        val responsestr = URL(url).readText()
        Log.d(javaClass.simpleName, responsestr)
        return responsestr

    }
}