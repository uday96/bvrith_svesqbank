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

class ResetPassword : AppCompatActivity() {

    private val webService = WebServiceUtil()

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
            Toast.makeText(this@ResetPassword, "Please go back and try again", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
            response?.isSuccessful.let {
                val resetStatus = response?.body().toString().trim()
                if (resetStatus == "success") {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@ResetPassword, R.drawable.ic_account_circle_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@ResetPassword, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@ResetPassword).setTitle("Password Reset Successful")
                        .setMessage("Your password has been reset to 123456")
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
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@ResetPassword, R.drawable.ic_warning_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@ResetPassword, android.R.color.holo_red_dark))
                    val alertDialog = AlertDialog.Builder(this@ResetPassword).setTitle("Password Reset Failed")
                        .setMessage("Please check your email and try again")
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
        setContentView(R.layout.activity_reset_password)

        val et_username = findViewById<EditText>(R.id.editText_resetpwd_email)
        val btn_reset = findViewById<Button>(R.id.button_resetpwd_reset)
        val btn_clear = findViewById<Button>(R.id.button_resetpwd_clear)

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

        btn_clear.setOnClickListener{
            et_username.text.clear()
            et_username.error = null
        }

        btn_reset.setOnClickListener {
            var valid = true
            if(et_username.text.isBlank()){
                et_username.error = getString(R.string.text_error_msg)
                valid = false
            }
            else if(!TextUtils.isEmailValid(et_username.text.toString().trim())){
                et_username.error = getString(R.string.email_error_msg)
                valid = false
            }
            if(valid){
                val uname = et_username.text.toString().trim()

                if (ConnectivityUtils.isConnected(this)) {
                    webService.resetPwd(uname,callback)
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
