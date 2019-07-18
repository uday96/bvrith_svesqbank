package com.example.bvrith_svesqbank.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
        val ques : ArrayList<Question> = intent.getSerializableExtra("questions") as ArrayList<Question>
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_logout -> {
                Log.d("DisplayAns","Logout")
                setResult(RESULT_OK)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
