package com.example.bvrith_svesqbank.ui.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.api.WebServiceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestions : AppCompatActivity(), AdapterView.OnItemClickListener {

    private val webService = WebServiceUtil()

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
            Log.e("MainActivity", "Problem calling API", t)
            Toast.makeText(this@FetchQuestions, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val subjects = response?.body().toString().trim().replace("\n","").dropLastWhile { it.equals(',') }
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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val dept = parent?.getItemAtPosition(position).toString()
        Log.d("FetchQue", dept)
        Toast.makeText(this@FetchQuestions, "Spinner selected : ${dept}", Toast.LENGTH_LONG).show()
        var subj_dropdown = findViewById(R.id.fetch_que_subj) as AutoCompleteTextView
        subj_dropdown.setText("")
        webService.getSubjects(dept,callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_questions)

        var res: Resources = resources
        var dept_dropdown = findViewById(R.id.fetch_que_dept) as AutoCompleteTextView
        var subj_dropdown = findViewById(R.id.fetch_que_subj) as AutoCompleteTextView
        var level_dropdown = findViewById(R.id.fetch_que_level) as AutoCompleteTextView
        var welcome_text = findViewById(R.id.textView_uname) as TextView
        var btn_fetch = findViewById(R.id.button_fetch_que) as Button

        val uname = intent.getStringExtra("uname")
        var welcome_msg =  "Hello, $uname"
        welcome_text.text = welcome_msg
        Log.d("FetchQue", welcome_msg)

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.dept_list,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            dept_dropdown.adapter = adapter
//        }
//        dept_dropdown.setSelection(0)
//        dept_dropdown.onItemSelectedListener = this

        val dept_list = res.getStringArray(R.array.dept_list)
        val dept_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, dept_list)
        dept_dropdown.setAdapter(dept_adapter)
        dept_dropdown.onItemClickListener = this

        val level_list = res.getStringArray(R.array.level_list)
        val level_adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, level_list)
        level_dropdown.setAdapter(level_adapter)




    }
}
