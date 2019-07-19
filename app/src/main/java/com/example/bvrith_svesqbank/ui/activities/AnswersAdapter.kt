package com.example.bvrith_svesqbank.ui.activities

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.data.Question

class AnswersAdapter(private val questions: ArrayList<Question>, private val selected_opts: IntArray) : RecyclerView.Adapter<AnswersAdapter.AnswersHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.answer_item, parent, false)
        return AnswersHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: AnswersHolder, position: Int) {
        holder.bindItems(position, questions[position], selected_opts[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return questions.size
    }

    //the class is hodling the list view
    class AnswersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(sno: Int, que: Question, my_opt: Int) {
            val que_title = itemView.findViewById(R.id.ansItem_que) as TextView
            val selected_ans = itemView.findViewById(R.id.ansItem_selected) as TextView
            val correct_ans = itemView.findViewById(R.id.ansItem_correct) as TextView
            val explanation = itemView.findViewById(R.id.ansItem_explanation) as TextView
            val resource = itemView.findViewById(R.id.ansItem_resource) as TextView
            val my_ans = when(my_opt){
                1 -> que.option1
                2 -> que.option2
                3 -> que.option3
                4 -> que.option4
                else -> "Invalid Response"
            }
            val ans = when(que.correct_answer!!.toInt()){
                1 -> que.option1
                2 -> que.option2
                3 -> que.option3
                4 -> que.option4
                else -> "Invalid Response"
            }
            val isCorrect = my_opt == que.correct_answer.toInt()
            var que_color = ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
            if(isCorrect){
                que_color = ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
            }
            que_title.text = SpannableStringBuilder()
                .bold { append((sno+1).toString() + ". " + que.question) }
            selected_ans.text = SpannableStringBuilder()
                .bold { append("Your Ans: ") }
                .color(que_color, { append(my_ans) })
            correct_ans.text = SpannableStringBuilder()
                .bold { append("Correct Ans: ") }
                .append(ans)
            explanation.text = SpannableStringBuilder()
                .bold { append("Explanation: ") }
                .append(que.explanation)
            resource.text = SpannableStringBuilder()
                .bold { append("Resource: ") }
                .append(que.resource)
        }
    }
}