package com.example.bvrith_svesqbank.ui.activities

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
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

class SignupActivity : AppCompatActivity() {

    private val webService = WebServiceUtil()

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
            Log.e("Signup", "Problem calling API", t)
            Toast.makeText(this@SignupActivity, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val loginStatus = response?.body().toString().trim()
                if (loginStatus == "success") {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@SignupActivity, R.drawable.ic_account_circle_black_24dp)!!);
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@SignupActivity, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@SignupActivity).setTitle("Account Created Successfully")
                        .setMessage("Please login to continue.")
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
                    alertDialog.setCanceledOnTouchOutside(false);
                }
                else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@SignupActivity, R.drawable.ic_warning_black_24dp)!!);
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@SignupActivity, android.R.color.holo_red_dark))
                    val alertDialog = AlertDialog.Builder(this@SignupActivity).setTitle("Login Failed")
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
        setContentView(R.layout.activity_signup)

        val res: Resources = resources
        val clg_dropdown = findViewById(R.id.spinner_signup_clg) as AutoCompleteTextView
        val role_dropdown = findViewById(R.id.spinner_signup_role) as AutoCompleteTextView
        val dept_dropdown = findViewById(R.id.spinner_signup_dept) as AutoCompleteTextView
        val sec_dropdown = findViewById(R.id.spinner_signup_sec) as AutoCompleteTextView
        val fullname = findViewById(R.id.editText_signup_fullname) as EditText
        val roll_num = findViewById(R.id.editText_signup_rollno) as EditText
        val email = findViewById(R.id.editText_signup_email) as EditText
        val mobile = findViewById(R.id.editText_signup_mobile) as EditText
        val uname = findViewById(R.id.editText_signup_uname) as EditText
        val pwd = findViewById(R.id.editText_signup_pwd) as EditText
        val btn_create = findViewById(R.id.button_signup_create) as Button
        val btn_clear = findViewById(R.id.button_signup_clear) as Button

        val colleges = res.getStringArray(R.array.signup_clg)
        val clg_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, colleges)
        clg_dropdown.setAdapter(clg_adapter)

        val roles = res.getStringArray(R.array.signup_role)
        val role_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, roles)
        role_dropdown.setAdapter(role_adapter)

        val depts = res.getStringArray(R.array.dept_list)
        val dept_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, depts)
        dept_dropdown.setAdapter(dept_adapter)

        val secs = res.getStringArray(R.array.signup_sec)
        val sec_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, secs)
        sec_dropdown.setAdapter(sec_adapter)

        clg_dropdown.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id->
            clg_dropdown.setError(null)
        }

        role_dropdown.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id->
            role_dropdown.setError(null)
        }

        dept_dropdown.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id->
            dept_dropdown.setError(null)
        }

        sec_dropdown.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id->
            sec_dropdown.setError(null)
        }


        fullname.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                fullname.setError(null)
                if(s.isNullOrBlank()){
                    fullname.setError(getString(R.string.text_error_msg))
                }
            }
        })

        roll_num.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                roll_num.setError(null)
                if(s.isNullOrBlank()){
                    roll_num.setError(getString(R.string.text_error_msg))
                }
            }
        })

        email.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                email.setError(null)
                if(s.isNullOrBlank()){
                    email.setError(getString(R.string.text_error_msg))
                }
            }
        })

        mobile.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                mobile.setError(null)
                if(s.isNullOrBlank()){
                    mobile.setError(getString(R.string.text_error_msg))
                }
            }
        })

        uname.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                uname.setError(null)
                if(s.isNullOrBlank()){
                    uname.setError(getString(R.string.text_error_msg))
                }
            }
        })

        pwd.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                pwd.setError(null)
                if(s.isNullOrBlank()){
                    pwd.setError(getString(R.string.text_error_msg))
                }
            }
        })

        btn_clear.setOnClickListener {
            fullname.text.clear()
            fullname.setError(null)
            roll_num.text.clear()
            roll_num.setError(null)
            email.text.clear()
            email.setError(null)
            mobile.text.clear()
            mobile.setError(null)
            uname.text.clear()
            uname.setError(null)
            pwd.text.clear()
            pwd.setError(null)
            clg_dropdown.text.clear()
            clg_dropdown.setError(null)
            role_dropdown.text.clear()
            role_dropdown.setError(null)
            dept_dropdown.text.clear()
            dept_dropdown.setError(null)
            sec_dropdown.text.clear()
            sec_dropdown.setError(null)

        }

        btn_create.setOnClickListener {
            var valid: Boolean = true
            if(fullname.text.isNullOrBlank()){
                fullname.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(roll_num.text.isNullOrBlank()){
                roll_num.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(email.text.isNullOrBlank()){
                email.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(mobile.text.isNullOrBlank()){
                mobile.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(uname.text.isNullOrBlank()){
                uname.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(pwd.text.isNullOrBlank()){
                pwd.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(clg_dropdown.text.isNullOrBlank()){
                clg_dropdown.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(role_dropdown.text.isNullOrBlank()){
                role_dropdown.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(dept_dropdown.text.isNullOrBlank()){
                dept_dropdown.setError(getString(R.string.text_error_msg))
                valid = false
            }
            if(sec_dropdown.text.isNullOrBlank()){
                sec_dropdown.setError(getString(R.string.text_error_msg))
                valid = false
            }

            if(valid){
                val fname = fullname.text.toString().trim()
                val rno = roll_num.text.toString().trim()
                val clg = clg_dropdown.text.toString().trim()
                val role = role_dropdown.text.toString().trim()
                val mail = email.text.toString().trim()
                val phno = mobile.text.toString().trim()
                val username = uname.text.toString().trim()
                val password = pwd.text.toString().trim()
                val dept = dept_dropdown.text.toString().trim()
                val sec = sec_dropdown.text.toString().trim()

                if (ConnectivityUtils.isConnected(this)) {
                    webService.signup(fname, rno, clg, role, mail, phno, username, password, dept, callback)
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


    }
}
