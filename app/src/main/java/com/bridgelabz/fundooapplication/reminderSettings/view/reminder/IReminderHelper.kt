package com.bridgelabz.fundooapplication.reminderSettings.view.reminder

import android.content.Context
import android.widget.EditText

interface IReminderHelper {
    fun selectDate(context: Context, inDate: EditText)
    fun selectTime(context: Context, inTime: EditText)
    fun setAlarm(context: Context,dateString: String, timeString: String, noteTitle: String, noteDescription: String)
}