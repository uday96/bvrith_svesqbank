package com.example.bvrith_svesqbank.ui.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.api.WebServiceUtil
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val webService = WebServiceUtil()

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
            Log.e("MainActivity", "Problem calling API", t)
            Toast.makeText(this@MainActivity, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val loginStatus = response?.body().toString().trim()
                if (loginStatus == "success") {
                    Snackbar.make(findViewById(R.id.constraintLayout), "Login Successful", Snackbar.LENGTH_LONG).show()
                    val fetchQueIntent = Intent(this@MainActivity, FetchQuestions::class.java);
                    startActivity(fetchQueIntent);
                }
                else {
                    Snackbar.make(findViewById(R.id.constraintLayout), "Login Failed: Please check your credentials", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get reference to all views
        var et_username = findViewById(R.id.editText_login_username) as EditText
        var et_password = findViewById(R.id.editText_login_pwd) as EditText
        var btn_login = findViewById(R.id.button_login) as Button
        var tv_forgotpwd = findViewById(R.id.textView_login_forgotpwd) as TextView
        var tv_resetpwd = findViewById(R.id.textView_login_resetpwd) as TextView
        var btn_signup = findViewById(R.id.button_signup) as Button

        // set on-click listener
        btn_login.setOnClickListener {
            Log.d("Main", "Login btn clicked")
            val uname = et_username.text.toString()
            val pwd = et_password.text.toString()

            // your code to validate the user_name and password combination
            // and verify the same
            if (isNetworkConnected()) {
//                webService.login(uname,pwd,callback)
                val fetchQueIntent = Intent(this@MainActivity, FetchQuestions::class.java);
                fetchQueIntent.putExtra("uname", uname)
                startActivity(fetchQueIntent);
            } else {
//                AlertDialog.Builder(this).setTitle("No Internet Connection")
//                    .setMessage("Please check your internet connection and try again")
//                    .setPositiveButton(android.R.string.ok) { _, _ -> }
//                    .setIcon(android.R.drawable.ic_dialog_alert).show()
                Snackbar.make(findViewById(R.id.constraintLayout), "No Internet Connection: Please check your internet connection and try again", Snackbar.LENGTH_LONG).show()
            }

        }

        btn_signup.setOnClickListener{
            Log.d("Main", "Signup btn clicked")
            val signupIntent = Intent(this, SignupActivity::class.java);
            startActivity(signupIntent);

        }

        tv_forgotpwd.setOnClickListener{
            Log.d("Main", "Forgot pwd btn clicked")
            Toast.makeText(this@MainActivity, "Forgot Password: Implementation Pending", Toast.LENGTH_LONG).show()
        }

        tv_resetpwd.setOnClickListener{
            Log.d("Main", "Reset pwd btn clicked")
            Toast.makeText(this@MainActivity, "Reset Password: Implementation Pending", Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
