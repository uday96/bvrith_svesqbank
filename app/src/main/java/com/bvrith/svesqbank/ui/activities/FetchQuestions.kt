package com.bvrith.svesqbank.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bvrith.svesqbank.R
import com.bvrith.svesqbank.api.WebServiceUtil
import com.bvrith.svesqbank.utils.ConnectivityUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestions : AppCompatActivity(){

    private val webService = WebServiceUtil()
    private var uname : String = ""
    private var dept : String = ""
    private var subj : String = ""
    private var unit : String = ""
    private var level : Int = -1
    private val REQUEST_CODE = 2

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
//            Log.e("MainActivity", "Problem calling API", t)
            Toast.makeText(this@FetchQuestions, "Please go back and try again", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
//            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val subjects = response?.body().toString().trim().replace("\n","").replace("\r","").dropLastWhile { it == ',' }
                val subj_list = subjects.split(",")
                val subj_dropdown = findViewById<AutoCompleteTextView>(R.id.fetch_que_subj)
                val subjAdapter = ArrayAdapter(this@FetchQuestions, R.layout.dropdown_menu_popup_item, subj_list)
                subjAdapter.notifyDataSetChanged()
                subj_dropdown.setAdapter(subjAdapter)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_questions)

        val res: Resources = resources
        val dept_dropdown = findViewById<AutoCompleteTextView>(R.id.fetch_que_dept)
        val unit_dropdown = findViewById<AutoCompleteTextView>(R.id.fetch_que_unit)
        val level_dropdown = findViewById<AutoCompleteTextView>(R.id.fetch_que_level)
        val welcome_text = findViewById<TextView>(R.id.textView_uname)
        val btn_fetch = findViewById<Button>(R.id.button_fetch_que)

        btn_fetch.isEnabled = false

        uname = intent.getStringExtra("uname")!!
        val welcome_msg =  "Hello, $uname"
        welcome_text.text = welcome_msg

        val dept_list = res.getStringArray(R.array.dept_list)
        val dept_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, dept_list)
        dept_dropdown.setAdapter(dept_adapter)
        dept_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dept = parent?.getItemAtPosition(position).toString()
                val subj_dropdown = findViewById<AutoCompleteTextView>(R.id.fetch_que_subj)
                subj_dropdown.setText("")
                subj = ""

                if (ConnectivityUtils.isConnected(this@FetchQuestions)) {
                    webService.getSubjects(dept,callback)
                } else {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@FetchQuestions, R.drawable.ic_warning_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@FetchQuestions, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@FetchQuestions).setTitle("No Internet Connection")
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

                validate_btn()
            }
        }

        val subj_dropdown = findViewById<AutoCompleteTextView>(R.id.fetch_que_subj)
        subj_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                subj = parent?.getItemAtPosition(position).toString()
                validate_btn()
            }
        }

        val unit_list = res.getStringArray(R.array.unit_list)
        val unit_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, unit_list)
        unit_dropdown.setAdapter(unit_adapter)
        unit_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                unit = parent?.getItemAtPosition(position).toString()
                validate_btn()
            }
        }

        val level_list = res.getStringArray(R.array.level_list)
        val level_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, level_list)
        level_dropdown.setAdapter(level_adapter)
        level_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                level = position
                validate_btn()
            }
        }

        btn_fetch.setOnClickListener{
            val displayQueIntent = Intent(this@FetchQuestions, DisplayQuestions::class.java)
            displayQueIntent.putExtra("uname", uname)
            displayQueIntent.putExtra("dept", dept)
            displayQueIntent.putExtra("subj", subj)
            displayQueIntent.putExtra("unit", unit)
            displayQueIntent.putExtra("level", level)
            startActivityForResult(displayQueIntent, REQUEST_CODE)
        }

    }

    fun validate_btn() {
        val btn_fetch = findViewById<Button>(R.id.button_fetch_que)
        btn_fetch.isEnabled = true
        if (dept.isEmpty() || subj.isEmpty() || unit.isEmpty() || level == -1){
            btn_fetch.isEnabled = false
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
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the requestCode is the wanted one and if the result is what we are expecting
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            finish()
        }
    }
}
