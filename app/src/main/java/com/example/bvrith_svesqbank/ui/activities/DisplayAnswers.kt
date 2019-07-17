package com.example.bvrith_svesqbank.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.data.Question

class DisplayAnswers : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_answers)

        val selected_opts = intent.getIntArrayExtra("selected_options")
        val ques : Array<Question> = intent.getSerializableExtra("questions") as Array<Question>
        val score = intent.getIntExtra("score", -1)

        val score_text = findViewById(R.id.textView_score) as TextView
        val recyclerView = findViewById(R.id.recyclerView_ans) as RecyclerView

        var score_msg =  "Test Score: $score"
        score_text.text = score_msg
        Log.d("DisplayAns", score_msg)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val adapter = AnswersAdapter(ques, selected_opts)

        recyclerView.adapter = adapter
    }
}
