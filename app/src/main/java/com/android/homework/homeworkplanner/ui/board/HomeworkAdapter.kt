package com.android.homework.homeworkplanner.ui.board

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.homework.homeworkplanner.R
import com.android.homework.homeworkplanner.data.Homework
import java.util.*
import kotlin.collections.ArrayList

class HomeworkAdapter(
    mContentList: ArrayList<Homework>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContentList: ArrayList<Homework>
    var initialHomeworkList = ArrayList<Homework>().apply {
        addAll(mContentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_homework, parent, false)
        return ViewHolder(view, viewType)
    }

    class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView
        val name: TextView
        val datetime: TextView
        val teacher: TextView
        val description: TextView
        val discipline: TextView
        val shareButton: Button

        init {
            // Find all views ids
            cardView = itemView.findViewById<View>(R.id.card_view_top) as CardView
            name = itemView.findViewById<View>(R.id.view_homework_name) as TextView
            datetime = itemView.findViewById<View>(R.id.view_homework_datetime) as TextView
            teacher = itemView.findViewById<View>(R.id.view_homework_teacher) as TextView
            description = itemView.findViewById<View>(R.id.view_homework_description) as TextView
            discipline = itemView.findViewById<View>(R.id.view_homework_discipline) as TextView
            shareButton = itemView.findViewById<View>(R.id.button_share) as Button

            shareButton.setOnClickListener { v ->
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                v.context.startActivity(shareIntent)
            }
        }
    }

    override fun onBindViewHolder(mainHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = mainHolder as ViewHolder
        val model: Homework = mContentList[position]
        // setting data over views
        val name: String = model.name.toString()
        val datetime: String = model.date.toString() + " " + model.time
        val teacher: String = model.teacher.toString()
        val description: String = model.description.toString()
        val discipline: String = model.discipline.toString()

        holder.name.text = name
        holder.datetime.text = datetime
        holder.teacher.text = teacher
        holder.discipline.text = discipline
        holder.description.text = description
    }

    override fun getItemCount(): Int {
        return mContentList.size
    }

    fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<Homework> = ArrayList()
            if(initialHomeworkList.isEmpty()){
                initialHomeworkList = ArrayList<Homework>().apply {
                    addAll(mContentList)
                }
            }
            if (constraint == null || constraint.isEmpty()) {
                initialHomeworkList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialHomeworkList.forEach {
                    if (it.name.toString().toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                mContentList.clear()
                mContentList.addAll(results.values as ArrayList<Homework>)
                notifyDataSetChanged()
            }
        }
    }

    init {
        this.mContentList = mContentList
    }
}