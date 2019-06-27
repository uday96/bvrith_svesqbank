package com.example.bvrith_svesqbank.ui.activities

import android.content.res.Resources
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bvrith_svesqbank.R

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val res: Resources = resources
        var clg_dropdown = findViewById(R.id.spinner_signup_clg) as AutoCompleteTextView
        var role_dropdown = findViewById(R.id.spinner_signup_role) as AutoCompleteTextView
        var dept_dropdown = findViewById(R.id.spinner_signup_dept) as AutoCompleteTextView
        var sec_dropdown = findViewById(R.id.spinner_signup_sec) as AutoCompleteTextView

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
    }
}
