package com.android.homework.homeworkplanner.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Homework(
    val discipline: String? = null,
    val teacher: String? = null,
    val description: String? = null,
    val name: String? = null,
    val date: String? = null,
    val time: String? = null
) {
}