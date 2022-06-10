package com.android.homework.homeworkplanner

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.homework.homeworkplanner.data.Homework
import com.android.homework.homeworkplanner.interfaces.OnDataPass
import com.android.homework.homeworkplanner.interfaces.OnTimePass
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddHomeworkActivity : AppCompatActivity(), OnDataPass, OnTimePass {

    private lateinit var database: DatabaseReference

    private lateinit var disciplineEditText: EditText
    private lateinit var teacherEditText: EditText
    private lateinit var homeworkNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private var date: String = String()
    private var time: String = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_homework)
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    fun addHomework(v: View) {
        disciplineEditText = findViewById(R.id.discipline)
        teacherEditText = findViewById(R.id.teacher)
        homeworkNameEditText = findViewById(R.id.homework_name)
        descriptionEditText = findViewById(R.id.description)

        if (disciplineEditText.text.isEmpty() || teacherEditText.text.isEmpty() || homeworkNameEditText.text.isEmpty() || descriptionEditText.text.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(applicationContext, "Please fill in all the fields", Toast.LENGTH_LONG)
                .show();
            return
        }

        database = Firebase.database.reference
        val homeworksRef: DatabaseReference = database.child("homeworks")

        val homeworks: MutableMap<String, Homework> = HashMap()
        homeworks[homeworksRef.push().key.toString()] = Homework(
            disciplineEditText.text.toString(),
            teacherEditText.text.toString(),
            descriptionEditText.text.toString(),
            homeworkNameEditText.text.toString(),
            date,
            time
        )

        homeworksRef.updateChildren(homeworks as Map<String, Any>)
        finish()
    }

    fun cancelHomework(v: View) {
        finish()
    }

    override fun onDataPass(year: Int, month: Int, day: Int) {
        date = "$year-$month-$day"
    }

    override fun onTimePass(hourOfDay: Int, minute: Int) {
        time = "$hourOfDay:$minute"
    }


}