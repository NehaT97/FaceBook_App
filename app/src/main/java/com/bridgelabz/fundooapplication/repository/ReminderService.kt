package com.bridgelabz.fundooapplication.repository

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.widget.EditText
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.IReminderService
import java.util.*


class ReminderService:IReminderService{

    override fun selectDate(context: Context, inDate: EditText) {
        val calender: Calendar = Calendar.getInstance()
        val mYear = calender.get(Calendar.YEAR)
        val mMonth = calender.get(Calendar.MONTH)
        val mDay = calender.get(Calendar.DAY_OF_MONTH)
        val reminderDialogue = Dialog(context)
        reminderDialogue.setContentView(R.layout.reminder_dialogue)

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                inDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    override fun selectTime(context: Context, inTime: EditText) {

        // Get Current Time
        val c = Calendar.getInstance()
        val mHour = c[Calendar.HOUR_OF_DAY]
        val mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            context,
            OnTimeSetListener { _, hourOfDay, minute -> inTime.setText("$hourOfDay:$minute") },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }


}