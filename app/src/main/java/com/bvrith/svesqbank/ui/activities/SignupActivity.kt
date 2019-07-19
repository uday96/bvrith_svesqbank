
package com.bvrith.svesqbank.ui.activities

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bvrith.svesqbank.R
import com.bvrith.svesqbank.api.WebServiceUtil
import com.bvrith.svesqbank.utils.ConnectivityUtils
import com.bvrith.svesqbank.utils.TextUtils
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private val webService = WebServiceUtil()

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
//            Log.e("Signup", "Problem calling API", t)
            Toast.makeText(this@SignupActivity, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
//            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val loginStatus = response?.body().toString().trim()
                if (loginStatus == "success") {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@SignupActivity, R.drawable.ic_account_circle_black_24dp)!!)
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
                    alertDialog.setCanceledOnTouchOutside(false)
                }
                else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@SignupActivity, R.drawable.ic_warning_black_24dp)!!)
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
        val clg_dropdown = findViewById<AutoCompleteTextView>(R.id.spinner_signup_clg)
        val role_dropdown = findViewById<AutoCompleteTextView>(R.id.spinner_signup_role)
        val dept_dropdown = findViewById<AutoCompleteTextView>(R.id.spinner_signup_dept)
        val sec_dropdown = findViewById<AutoCompleteTextView>(R.id.spinner_signup_sec)
        val fullname = findViewById<EditText>(R.id.editText_signup_fullname)
        val roll_num = findViewById<EditText>(R.id.editText_signup_rollno)
        val email = findViewById<EditText>(R.id.editText_signup_email)
        val mobile = findViewById<EditText>(R.id.editText_signup_mobile)
        val uname = findViewById<EditText>(R.id.editText_signup_uname)
        val pwd = findViewById<EditText>(R.id.editText_signup_pwd)
        val btn_create = findViewById<Button>(R.id.button_signup_create)
        val btn_clear = findViewById<Button>(R.id.button_signup_clear)
        val layout_dept = findViewById<TextInputLayout>(R.id.layout_signup_dept)
        val layout_sec = findViewById<TextInputLayout>(R.id.layout_signup_sec)

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

        clg_dropdown.onItemClickListener = AdapterView.OnItemClickListener{_,_,_,_ ->
            clg_dropdown.error = null
        }

        role_dropdown.onItemClickListener = AdapterView.OnItemClickListener{parent,_,position,_ ->
            role_dropdown.error = null
            when(parent.getItemAtPosition(position).toString()) {
                "Faculty" -> {
                    layout_sec.visibility = View.GONE
                    layout_dept.visibility = View.VISIBLE
                }
                "Principal/Director" -> {
                    layout_sec.visibility = View.GONE
                    layout_dept.visibility = View.GONE
                }
                else -> {
                    layout_sec.visibility = View.VISIBLE
                    layout_dept.visibility = View.VISIBLE
                }
            }
        }

        dept_dropdown.onItemClickListener = AdapterView.OnItemClickListener{_,_,_,_ ->
            dept_dropdown.error = null
        }

        sec_dropdown.onItemClickListener = AdapterView.OnItemClickListener{_,_,_,_ ->
            sec_dropdown.error = null
        }


        fullname.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                fullname.error = null
                if(s.isBlank()){
                    fullname.error = getString(R.string.text_error_msg)
                }
            }
        })

        roll_num.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                roll_num.error = null
                if(s.isBlank()){
                    roll_num.error = getString(R.string.text_error_msg)
                }
            }
        })

        email.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                email.error = null
                if(s.isBlank()){
                    email.error = getString(R.string.text_error_msg)
                }
            }
        })

        mobile.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                mobile.error = null
                if(s.isBlank()){
                    mobile.error = getString(R.string.text_error_msg)
                }
            }
        })

        uname.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                uname.error = null
                if(s.isBlank()){
                    uname.error = getString(R.string.text_error_msg)
                }
            }
        })

        pwd.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                pwd.error = null
                if(s.isBlank()){
                    pwd.error = getString(R.string.text_error_msg)
                }
            }
        })

        btn_clear.setOnClickListener {
            fullname.text.clear()
            fullname.error = null
            roll_num.text.clear()
            roll_num.error = null
            email.text.clear()
            email.error = null
            mobile.text.clear()
            mobile.error = null
            uname.text.clear()
            uname.error = null
            pwd.text.clear()
            pwd.error = null
            clg_dropdown.text.clear()
            clg_dropdown.error = null
            role_dropdown.text.clear()
            role_dropdown.error = null
            dept_dropdown.text.clear()
            dept_dropdown.error = null
            sec_dropdown.text.clear()
            sec_dropdown.error = null

        }

        btn_create.setOnClickListener {
            var valid = true
            if(fullname.text.isBlank()){
                fullname.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(roll_num.text.isBlank()){
                roll_num.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(email.text.isBlank()){
                email.error = getString(R.string.text_error_msg)
                valid = false
            }
            else if(!TextUtils.isEmailValid(email.text.toString().trim())){
                email.error = getString(R.string.email_error_msg)
                valid = false
            }
            if(mobile.text.isBlank()){
                mobile.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(uname.text.isBlank()){
                uname.error = getString(R.string.text_error_msg)
                valid = false
            }
            else if(!TextUtils.isEmailValid(uname.text.toString().trim())){
                uname.error = getString(R.string.email_error_msg)
                valid = false
            }
            if(pwd.text.isBlank()){
                pwd.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(clg_dropdown.text.isBlank()){
                clg_dropdown.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(role_dropdown.text.isBlank()){
                role_dropdown.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(dept_dropdown.text.isBlank() && layout_dept.visibility == View.VISIBLE){
                dept_dropdown.error = getString(R.string.text_error_msg)
                valid = false
            }
            if(sec_dropdown.text.isBlank() && layout_sec.visibility == View.VISIBLE){
                sec_dropdown.error = getString(R.string.text_error_msg)
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
                var dept = dept_dropdown.text.toString().trim()
                var sec = sec_dropdown.text.toString().trim()

                when(role) {
                    "Faculty" -> {
                        sec = ""
                    }
                    "Principal/Director" -> {
                        sec = ""
                        dept = ""
                    }
                    else -> {
                        assert(sec.isNotBlank())
                        assert(dept.isNotBlank())
                    }
                }

                if (ConnectivityUtils.isConnected(this)) {
                    webService.signup(fname, rno, clg, role, mail, phno, username, password, dept, sec, callback)
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
}
