package com.bvrith.svesqbank.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.bvrith.svesqbank.R
import com.bvrith.svesqbank.api.WebServiceUtil
import com.bvrith.svesqbank.utils.ConnectivityUtils
import com.bvrith.svesqbank.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val webService = WebServiceUtil()
    private var uname: String = ""

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
//            Log.e("MainActivity", "Problem calling API", t)
            Toast.makeText(this@MainActivity, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
//            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val loginStatus = response?.body().toString().trim()
                if (loginStatus == "success") {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_account_circle_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@MainActivity).setTitle("Welcome")
                        .setMessage("Logged in successfully!")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            val fetchQueIntent = Intent(this@MainActivity, FetchQuestions::class.java)
                            fetchQueIntent.putExtra("uname", uname)
                            startActivity(fetchQueIntent)
                        }
                        .setIcon(drawable)
                        .show()
                    val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    with(button) {
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        setTextColor(Color.WHITE)
                    }
                    alertDialog.setCanceledOnTouchOutside(false)
                }
                else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_warning_black_24dp)!!)
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
        val et_username = findViewById<EditText>(R.id.editText_login_username)
        val et_password = findViewById<EditText>(R.id.editText_login_pwd)
        val btn_login = findViewById<Button>(R.id.button_login)
        val btn_clear = findViewById<Button>(R.id.button_login_cancel)
        val tv_forgotpwd = findViewById<TextView>(R.id.textView_login_forgotpwd)
        val tv_resetpwd = findViewById<TextView>(R.id.textView_login_resetpwd)
        val btn_signup = findViewById<Button>(R.id.button_signup)

        et_username.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                et_username.error = null
                if(s.isBlank()){
                    et_username.error = getString(R.string.text_error_msg)
                }
            }
        })

        et_password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                et_password.error = null
                if(s.isBlank()){
                    et_password.error = getString(R.string.text_error_msg)
                }
            }
        })

        btn_clear.setOnClickListener{
            et_username.text.clear()
            et_password.text.clear()
            et_username.error = null
            et_password.error = null
        }

        // set on-click listener
        btn_login.setOnClickListener {
            var valid = true
            if(et_username.text.isBlank()){
                et_username.error = getString(R.string.text_error_msg)
                valid = false
            }
            else if(!TextUtils.isEmailValid(et_username.text.toString().trim())){
                et_username.error = getString(R.string.email_error_msg)
                valid = false
            }
            if(et_password.text.isBlank()){
                et_password.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(valid){
                uname = et_username.text.toString()
                val pwd = et_password.text.toString()

                if (ConnectivityUtils.isConnected(this)) {
                    webService.login(uname,pwd,callback)
                } else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_warning_black_24dp)!!)
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
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)

        }

        tv_forgotpwd.setOnClickListener{
            Toast.makeText(this@MainActivity, "Forgot Password: Implementation Pending", Toast.LENGTH_LONG).show()
        }

        tv_resetpwd.setOnClickListener{
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
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
