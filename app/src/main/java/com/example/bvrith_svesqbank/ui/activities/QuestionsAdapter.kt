package com.example.bvrith_svesqbank.ui.activities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bvrith_svesqbank.R
import com.example.bvrith_svesqbank.data.Question

class QuestionsAdapter(val questions: Array<Question>, val selected_opts: HashMap<Int,Int>) : RecyclerView.Adapter<QuestionsAdapter.QuestionsHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsAdapter.QuestionsHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return QuestionsHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: QuestionsAdapter.QuestionsHolder, position: Int) {
        holder.bindItems(position, questions[position], selected_opts)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return questions.size
    }

    //the class is hodling the list view
    class QuestionsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(sno: Int, que: Question, selected_opts: HashMap<Int,Int>) {
            val que_title = itemView.findViewById(R.id.queItem_que) as TextView
            val options = itemView.findViewById(R.id.queItem) as RadioGroup
            val option1 = itemView.findViewById(R.id.queItem_option1) as RadioButton
            val option2 = itemView.findViewById(R.id.queItem_option2) as RadioButton
            val option3 = itemView.findViewById(R.id.queItem_option3) as RadioButton
            val option4 = itemView.findViewById(R.id.queItem_option4) as RadioButton
            que_title.text = (sno+1).toString() + ". " + que.question
            option1.text = que.option1
            option2.text = que.option2
            option3.text = que.option3
            option4.text = que.option4

            options.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener {group, checkedId ->
                val radio: RadioButton = itemView.findViewById(checkedId)
                selected_opts[sno+1] = group.indexOfChild(radio)+1
                Log.d("OptionsHM", selected_opts.toString())
            })
        }
    }
}