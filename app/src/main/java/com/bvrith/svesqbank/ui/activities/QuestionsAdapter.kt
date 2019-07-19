package com.bvrith.svesqbank.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bvrith.svesqbank.R
import com.bvrith.svesqbank.data.Question

class QuestionsAdapter(private val questions: ArrayList<Question>, private val selected_opts: HashMap<Int,Int>) : RecyclerView.Adapter<QuestionsAdapter.QuestionsHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return QuestionsHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: QuestionsHolder, position: Int) {
        holder.bindItems(position, questions[position], selected_opts)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return questions.size
    }

    //the class is holding the list view
    class QuestionsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(sno: Int, que: Question, selected_opts: HashMap<Int,Int>) {
            val que_title = itemView.findViewById<TextView>(R.id.queItem_que)
            val options = itemView.findViewById<RadioGroup>(R.id.queItem)
            val option1 = itemView.findViewById<RadioButton>(R.id.queItem_option1)
            val option2 = itemView.findViewById<RadioButton>(R.id.queItem_option2)
            val option3 = itemView.findViewById<RadioButton>(R.id.queItem_option3)
            val option4 = itemView.findViewById<RadioButton>(R.id.queItem_option4)
            val que_text = "${(sno+1)}. ${que.question}"
            que_title.text = que_text
            option1.text = que.option1
            option2.text = que.option2
            option3.text = que.option3
            option4.text = que.option4

            options.setOnCheckedChangeListener {group, checkedId ->
                if(checkedId > 0){
                    val radio: RadioButton = itemView.findViewById(checkedId)
                    selected_opts[sno+1] = group.indexOfChild(radio)+1
                    option1.error = null
                }
            }
        }
    }
}