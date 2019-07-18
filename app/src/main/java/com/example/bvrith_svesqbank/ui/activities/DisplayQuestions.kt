package com.example.bvrith_svesqbank.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.api.ConnectivityUtils
import com.example.bvrith_svesqbank.api.WebServiceUtil
import com.example.bvrith_svesqbank.data.Question
import com.example.bvrith_svesqbank.data.Questions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayQuestions : AppCompatActivity() {

    private val webService = WebServiceUtil()
    private val REQUEST_CODE = 3
    private var uname: String = ""
    private var subj: String = ""

    val selected_opts: HashMap<Int,Int> = hashMapOf()
    val questions: ArrayList<Question> = ArrayList()
    private lateinit var adapter: QuestionsAdapter

    private val callback = object : Callback<Questions> {
        override fun onFailure(call: Call<Questions>?, t: Throwable?) {
            Log.e("DisplayQue", "Problem calling API", t)
            Toast.makeText(this@DisplayQuestions, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<Questions>?, response: Response<Questions>?) {
            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                questions.addAll(response?.body()!!.questions)
                adapter.notifyDataSetChanged()
                val layout = findViewById(R.id.layout_que) as LinearLayout
                layout.setVisibility(View.VISIBLE)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_questions)

        uname = intent.getStringExtra("uname")
        val dept = intent.getStringExtra("dept")
        subj = intent.getStringExtra("subj")
        val level = intent.getIntExtra("level", -1) + 1
        Log.d("DisplayQue", "Dept: ${dept} , Subj: ${subj}, Level: ${level}")

        if (ConnectivityUtils.isConnected(this@DisplayQuestions)) {
            webService.getQuestions(dept, subj, level, callback)
        } else {
            val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_warning_black_24dp)!!);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorPrimary))
            val alertDialog = AlertDialog.Builder(this@DisplayQuestions).setTitle("No Internet Connection")
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

        val recyclerView = findViewById(R.id.recyclerView_que) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter = QuestionsAdapter(questions, selected_opts)
        recyclerView.adapter = adapter

        val submit = findViewById(R.id.button_que_submit) as Button

        submit.setOnClickListener{
            Log.d("DisplayQue", selected_opts.toString())
            val isValid = validate(selected_opts, questions.size)
            Log.d("DisplayQue", isValid.toString())
            if(isValid == -1){
                score(selected_opts, questions)
            }
        }

        val layout = findViewById(R.id.layout_que) as LinearLayout
        layout.setVisibility(View.GONE)
    }

    fun validate(selected_opts: HashMap<Int,Int>, total: Int): Int{
        if(selected_opts.size == total){
            return -1
        }
        for(qno in 1..total){
            if(!selected_opts.containsKey(qno-1)){
                return qno-1
            }
        }
        return -1
    }

    fun score(selected_opts: HashMap<Int,Int>, questions: ArrayList<Question>) {
        val total = questions.size
        var score = 0
        val selected_options = IntArray(total)
        for(qno in 1..total){
            selected_options[qno-1] = selected_opts[qno]!!
            if(questions[qno-1].correct_answer?.toInt() == selected_opts[qno]){
                score += 1
            }
        }

        val displayAnsIntent = Intent(this@DisplayQuestions, DisplayAnswers::class.java);
        displayAnsIntent.putExtra("questions", questions)
        displayAnsIntent.putExtra("selected_options", selected_options)
        displayAnsIntent.putExtra("score", score)
        displayAnsIntent.putExtra("subj", subj)
        displayAnsIntent.putExtra("uname", uname)
        startActivityForResult(displayAnsIntent, REQUEST_CODE);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_logout -> {
                Log.d("DisplayQue","Logout")
                setResult(RESULT_OK)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // check if the requestCode is the wanted one and if the result is what we are expecting
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("DisplayQue","Logout - Back")
            setResult(RESULT_OK)
            finish()
        }
    }
}
