package com.bvrith.svesqbank.ui.activities

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
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

class ForgotPassword : AppCompatActivity() {

    private val webService = WebServiceUtil()
    var changed_pwd = ""

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
            Toast.makeText(this@ForgotPassword, "Please go back and try again", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
            response?.isSuccessful.let {
                val status = response?.body().toString().trim()
                if (status == "success") {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@ForgotPassword, R.drawable.ic_account_circle_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@ForgotPassword, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@ForgotPassword).setTitle("Password Change Successful")
                        .setMessage("Your password has been changed to $changed_pwd")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            finish()
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
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@ForgotPassword, R.drawable.ic_warning_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@ForgotPassword, android.R.color.holo_red_dark))
                    val alertDialog = AlertDialog.Builder(this@ForgotPassword).setTitle("Password Change Failed")
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
        setContentView(R.layout.activity_forgot_password)

        val et_username = findViewById<EditText>(R.id.editText_forgotpwd_email)
        val et_pwd_new = findViewById<EditText>(R.id.editText_forgotpwd_pwd)
        val et_pwd_confirm = findViewById<EditText>(R.id.editText_forgotpwd_pwd_confirm)
        val btn_submit = findViewById<Button>(R.id.button_forgotpwd_submit)
        val btn_clear = findViewById<Button>(R.id.button_forgotpwd_clear)

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

        et_pwd_new.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                et_pwd_new.error = null
                if(s.isBlank()){
                    et_pwd_new.error = getString(R.string.text_error_msg)
                }
            }
        })

        et_pwd_confirm.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                et_pwd_confirm.error = null
                if(s.isBlank()){
                    et_pwd_confirm.error = getString(R.string.text_error_msg)
                }
                else if(et_pwd_new.text.isNotBlank()){
                    val pwd_new = et_pwd_new.text.toString().trim()
                    val pwd_confirm = s.toString().trim()
                    if(pwd_new != pwd_confirm){
                        et_pwd_confirm.error = getString(R.string.pwd_error_msg)
                    }
                }
            }
        })

        btn_clear.setOnClickListener{
            et_username.text.clear()
            et_username.error = null
            et_pwd_new.text.clear()
            et_pwd_new.error = null
            et_pwd_confirm.text.clear()
            et_pwd_confirm.error = null
        }

        btn_submit.setOnClickListener {
            var valid = true
            if(et_username.text.isBlank()){
                et_username.error = getString(R.string.text_error_msg)
                valid = false
            }
            else if(!TextUtils.isEmailValid(et_username.text.toString().trim())){
                et_username.error = getString(R.string.email_error_msg)
                valid = false
            }
            if(et_pwd_new.text.isBlank()){
                et_pwd_new.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(et_pwd_confirm.text.isBlank()){
                et_pwd_confirm.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(et_pwd_new.text.isNotBlank() && et_pwd_confirm.text.isNotBlank()){
                val pwd_new = et_pwd_new.text.toString().trim()
                val pwd_confirm = et_pwd_confirm.text.toString().trim()
                if(pwd_new != pwd_confirm){
                    et_pwd_confirm.error = getString(R.string.pwd_error_msg)
                    valid = false
                }
            }
            if(valid){
                val uname = et_username.text.toString().trim()
                val pwd = et_pwd_new.text.toString().trim()
                changed_pwd = pwd

                if (ConnectivityUtils.isConnected(this)) {
                    webService.forgotPwd(uname, pwd, callback)
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
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN){
            val vw = currentFocus
            if(vw is EditText){
                val outRect = Rect()
                vw.getGlobalVisibleRect(outRect)
                if(!outRect.contains(ev.rawX.toInt(),ev.rawY.toInt())){
                    vw.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(vw.windowToken,0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
