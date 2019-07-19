package com.bvrith.svesqbank.ui.activities

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bvrith.svesqbank.R
import com.bvrith.svesqbank.api.WebServiceUtil
import com.bvrith.svesqbank.data.Question
import com.bvrith.svesqbank.ui.adapters.AnswersAdapter
import com.bvrith.svesqbank.utils.ConnectivityUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayAnswers : AppCompatActivity() {

    private val webService = WebServiceUtil()
    private var score: Int = -1
    private var total: Int = -1

    private val callback = object : Callback<String> {
        override fun onFailure(call: Call<String>?, t: Throwable?) {
//            Log.e("DisplayAns", "Problem calling API", t)
            Toast.makeText(this@DisplayAnswers, "Error sending scores", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<String>?, response: Response<String>?) {
//            Log.i("Response", response?.body().toString());
            response?.isSuccessful.let {
                val status = response?.body().toString().trim()
                if (status == "success") {
                    val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this@DisplayAnswers, R.drawable.ic_check_circle_black_24dp)!!)
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(this@DisplayAnswers, R.color.colorPrimary))
                    val alertDialog = AlertDialog.Builder(this@DisplayAnswers).setTitle("Test Score")
                        .setMessage("Your test score is $score / $total")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            val layout = findViewById<LinearLayout>(R.id.layout_ans)
                            layout.visibility = View.VISIBLE
                        }
                        .setIcon(drawable)
                        .show()
                    val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    with(button) {
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        setTextColor(Color.WHITE)
                    }
                }
                else{
                    Toast.makeText(this@DisplayAnswers, "Error sending scores", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_answers)

        val selected_opts = intent.getIntArrayExtra("selected_options")
        @Suppress("UNCHECKED_CAST")
        val ques : ArrayList<Question> = intent.getSerializableExtra("questions") as ArrayList<Question>
        score = intent.getIntExtra("score", -1)
        total = ques.size
        val uname = intent.getStringExtra("uname")
        val subj = intent.getStringExtra("subj")

        val score_text = findViewById<TextView>(R.id.textView_score)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_ans)
        val layout = findViewById<LinearLayout>(R.id.layout_ans)

        layout.visibility = View.GONE

        val score_msg =  "Test Score: $score"
        score_text.text = score_msg

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val adapter = AnswersAdapter(ques, selected_opts!!)

        recyclerView.adapter = adapter

        if (ConnectivityUtils.isConnected(this@DisplayAnswers)) {
            webService.sendScore(uname!!, subj!!, score, callback)
        } else {
            val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_warning_black_24dp)!!)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorPrimary))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_logout -> {
                setResult(RESULT_OK)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
