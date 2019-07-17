package com.example.bvrith_svesqbank.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.api.WebServiceUtil
import com.example.bvrith_svesqbank.data.Question
import com.example.bvrith_svesqbank.data.Questions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayQuestions : AppCompatActivity() {

    private val webService = WebServiceUtil()

    private val callback = object : Callback<Questions> {
        override fun onFailure(call: Call<Questions>?, t: Throwable?) {
            Log.e("DisplayQue", "Problem calling API", t)
            Toast.makeText(this@DisplayQuestions, "Error calling API", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<Questions>?, response: Response<Questions>?) {
            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_questions)

        val dept = intent.getStringExtra("dept")
        val subj = intent.getStringExtra("subj")
        val level = intent.getIntExtra("level", -1) + 1
        Log.d("DisplayQue", "Dept: ${dept} , Subj: ${subj}, Level: ${level}")

//        if (ConnectivityUtils.isConnected(this@DisplayQuestions)) {
//            webService.getQuestions(dept, subj, level, callback)
//        } else {
//            AlertDialog.Builder(this@DisplayQuestions).setTitle("No Internet Connection")
//                .setMessage("Please check your internet connection and try again")
//                .setPositiveButton(android.R.string.ok) { _, _ -> }
//                .setIcon(android.R.drawable.ic_dialog_alert).show()
//        }

        val recyclerView = findViewById(R.id.recyclerView_que) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val ques = arrayOf<Question>(Question("842",
            "Which of these is a mechanism for naming and visibility control of a class and its content?",
            "Object",
            "Packages",
            "Interfaces",
            "None of the Mentioned.",
            "2",
            "Packages are both naming and visibility control mechanism. We can define a class inside a package which is not accessible by code outside the package.",
            "https://www.indiabix.com"),
            Question("845",
                "Which of these is a mechanism for naming and visibility control of a class and its content?",
                "Object",
                "Packages",
                "Interfaces",
                "None of the Mentioned.",
                "2",
                "Packages are both naming and visibility control mechanism. We can define a class inside a package which is not accessible by code outside the package.",
                "https://www.indiabix.com"))

        val selected_opts: HashMap<Int,Int> = hashMapOf()

        val adapter = QuestionsAdapter(ques, selected_opts)

        recyclerView.adapter = adapter

        val submit = findViewById(R.id.button_que_submit) as Button

        submit.setOnClickListener{
            Log.d("DisplayQue", selected_opts.toString())
            val isValid = validate(selected_opts, ques.size)
            Log.d("DisplayQue", isValid.toString())
            if(isValid == -1){
                score(selected_opts, ques)
            }
        }
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

    fun score(selected_opts: HashMap<Int,Int>, questions: Array<Question>) {
        val total = questions.size
        var score = 0
        val selected_options = IntArray(total)
        for(qno in 1..total){
            selected_options[qno-1] = selected_opts[qno]!!
            if(questions[qno-1].correct_answer?.toInt() == selected_opts[qno]){
                score += 1
            }
        }

        AlertDialog.Builder(this@DisplayQuestions).setTitle("Test Score")
                .setMessage("Your test score is ${score} / ${total}")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val displayAnsIntent = Intent(this@DisplayQuestions, DisplayAnswers::class.java);
                    displayAnsIntent.putExtra("questions", questions)
                    displayAnsIntent.putExtra("selected_options", selected_options)
                    displayAnsIntent.putExtra("score", score)
                    startActivity(displayAnsIntent);
                }
                .show()
    }
}
