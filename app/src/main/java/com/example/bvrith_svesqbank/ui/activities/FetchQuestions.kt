package com.example.bvrith_svesqbank.ui.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.api.ConnectivityUtils
import com.example.bvrith_svesqbank.api.WebServiceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestions : AppCompatActivity(){

    private val webService = WebServiceUtil()
    private var dept : String = ""
    private var subj : String = ""
    private var level : Int = -1

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
            Log.e("MainActivity", "Problem calling API", t)
            Toast.makeText(this@FetchQuestions, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val subjects = response?.body().toString().trim().replace("\n","").replace("\r","").dropLastWhile { it.equals(',') }
                val subj_list = subjects.split(",")
                Log.d("Subjects", subj_list.toString());
                var subj_dropdown = findViewById(R.id.fetch_que_subj) as AutoCompleteTextView
                val subjAdapter = ArrayAdapter(this@FetchQuestions, R.layout.dropdown_menu_popup_item, subj_list)
                subjAdapter.notifyDataSetChanged()
                subj_dropdown.setAdapter(subjAdapter)
//                subj_dropdown.setText(subjAdapter.getItem(0).toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_questions)

        var res: Resources = resources
        var dept_dropdown = findViewById(R.id.fetch_que_dept) as AutoCompleteTextView
        var level_dropdown = findViewById(R.id.fetch_que_level) as AutoCompleteTextView
        var welcome_text = findViewById(R.id.textView_uname) as TextView
        var btn_fetch = findViewById(R.id.button_fetch_que) as Button

        btn_fetch.isEnabled = false

        val uname = intent.getStringExtra("uname")
        var welcome_msg =  "Hello, $uname"
        welcome_text.text = welcome_msg
        Log.d("FetchQue", welcome_msg)

        // TODO : remove
        val displayQueIntent = Intent(this@FetchQuestions, DisplayQuestions::class.java);
        displayQueIntent.putExtra("dept", "CSE/IT")
        displayQueIntent.putExtra("subj", "JAVA")
        displayQueIntent.putExtra("level", 0)
        startActivity(displayQueIntent);

        val dept_list = res.getStringArray(R.array.dept_list)
        val dept_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, dept_list)
        dept_dropdown.setAdapter(dept_adapter)
        dept_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dept = parent?.getItemAtPosition(position).toString()
                Log.d("FetchQue", dept)
                Toast.makeText(this@FetchQuestions, "Spinner selected : ${dept}", Toast.LENGTH_LONG).show()
                var subj_dropdown = findViewById(R.id.fetch_que_subj) as AutoCompleteTextView
                subj_dropdown.setText("")
                subj = ""

                if (ConnectivityUtils.isConnected(this@FetchQuestions)) {
                    webService.getSubjects(dept,callback)
                } else {
                    AlertDialog.Builder(this@FetchQuestions).setTitle("No Internet Connection")
                        .setMessage("Please check your internet connection and try again")
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .setIcon(android.R.drawable.ic_dialog_alert).show()
                }

                validate_btn()
            }
        }

        val subj_dropdown = findViewById(R.id.fetch_que_subj) as AutoCompleteTextView
        subj_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                subj = parent?.getItemAtPosition(position).toString()
                Log.d("FetchQue", subj)
                validate_btn()
                Toast.makeText(this@FetchQuestions, "Spinner selected : ${subj}", Toast.LENGTH_LONG).show()
            }
        }

        val level_list = res.getStringArray(R.array.level_list)
        val level_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, level_list)
        level_dropdown.setAdapter(level_adapter)
        level_dropdown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val level_val = parent?.getItemAtPosition(position).toString()
                level = position
                Log.d("FetchQue", level_val)
                validate_btn()
                Toast.makeText(this@FetchQuestions, "Spinner selected : ${level}", Toast.LENGTH_LONG).show()
            }
        }

        btn_fetch.setOnClickListener{
            Log.d("FetchQue", "Fetch Que btn clicked")
            Log.d("FetchQue", "Dept: ${dept} , Subj: ${subj}, Level: ${level}")
            val displayQueIntent = Intent(this@FetchQuestions, DisplayQuestions::class.java);
            displayQueIntent.putExtra("dept", dept)
            displayQueIntent.putExtra("subj", subj)
            displayQueIntent.putExtra("level", level)
            startActivity(displayQueIntent);
        }

    }

    fun validate_btn() {
        var btn_fetch = findViewById(R.id.button_fetch_que) as Button
        btn_fetch.isEnabled = true
        if (dept.isEmpty() || subj.isEmpty() || level == -1){
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
                Log.d("Fetch","Logout")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
