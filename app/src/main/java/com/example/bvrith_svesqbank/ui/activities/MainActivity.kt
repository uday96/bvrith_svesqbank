package com.example.bvrith_svesqbank.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.api.ConnectivityUtils
import com.example.bvrith_svesqbank.api.WebServiceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val webService = WebServiceUtil()
    private var uname: String = ""

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
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_account_circle_black_24dp)!!);
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@MainActivity).setTitle("Welcome")
                        .setMessage("Logged in successfully!")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            val fetchQueIntent = Intent(this@MainActivity, FetchQuestions::class.java);
                            fetchQueIntent.putExtra("uname", uname)
                            startActivity(fetchQueIntent);
                        }
                        .setIcon(drawable)
                        .show()
                    val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    with(button) {
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        setTextColor(Color.WHITE)
                    }
                    alertDialog.setCanceledOnTouchOutside(false);
                }
                else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_warning_black_24dp)!!);
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@MainActivity, android.R.color.holo_red_dark))
                    val alertDialog = AlertDialog.Builder(this@MainActivity).setTitle("Login Failed")
                        .setMessage("Please check your credentials and try again")
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .setIcon(drawable)
                        .show()
                    val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    with(button) {
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        setTextColor(Color.WHITE)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar?.hide()

        // get reference to all views
        var et_username = findViewById(R.id.editText_login_username) as EditText
        var et_password = findViewById(R.id.editText_login_pwd) as EditText
        var btn_login = findViewById(R.id.button_login) as Button
        var btn_clear = findViewById(R.id.button_login_cancel) as Button
        var tv_forgotpwd = findViewById(R.id.textView_login_forgotpwd) as TextView
        var tv_resetpwd = findViewById(R.id.textView_login_resetpwd) as TextView
        var btn_signup = findViewById(R.id.button_signup) as Button

        et_username.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                et_username.setError(null)
                if(s.length == 0){
                    et_username.setError("Field cannot be empty")
                }
            }
        })

        et_password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                et_password.setError(null)
                if(s.length == 0){
                    et_password.setError("Field cannot be empty")
                }
            }
        })

        btn_clear.setOnClickListener{
            et_username.text.clear()
            et_password.text.clear()
            et_username.setError(null)
            et_password.setError(null)
        }

        // set on-click listener
        btn_login.setOnClickListener {
            Log.d("Main", "Login btn clicked")
            var valid = true
            if(et_username.text.isNullOrBlank()){
                et_username.setError("Field cannot be empty")
                valid = false
            }
            if(et_password.text.isNullOrBlank()){
                et_password.setError("Field cannot be empty")
                valid = false
            }
            if(valid){
                uname = et_username.text.toString()
                val pwd = et_password.text.toString()

                if (ConnectivityUtils.isConnected(this)) {
                    webService.login(uname,pwd,callback)
                } else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_warning_black_24dp)!!);
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this, android.R.color.holo_orange_dark))
                    val alertDialog = AlertDialog.Builder(this).setTitle("No Internet Connection")
                        .setMessage("Please check your internet connection and try again")
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .setIcon(drawable)
                        .show()
                    val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    with(button) {
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        setTextColor(Color.WHITE)
                    }
                }
            }
        }

        btn_signup.setOnClickListener{
            Log.d("Main", "Signup btn clicked")
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_logout -> {
                Log.d("Main","Logout")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
