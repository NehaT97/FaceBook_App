package com.bridgelabz.fundooapplication.reminderSettings.view.reminder

import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.reminderSettings.view.alarmReceiver.MyAlarm
import java.time.*
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates


class ReminderHelper : IReminderHelper {

    override fun selectDate(context: Context, inDate: EditText) {
        val calender = Calendar.getInstance()
        val mYear = calender.get(Calendar.YEAR)
        val mMonth = calender.get(Calendar.MONTH)
        val mDay = calender.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val dateString = context.getString(
                    R.string.date_place_holder,
                    dayOfMonth.toString(),
                    monthOfYear.toString(),
                    year.toString()
                )
                inDate.setText(dateString)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    override fun selectTime(context: Context, inTime: EditText) {
        val calender = Calendar.getInstance()
        val mHour = calender[Calendar.HOUR_OF_DAY]
        val mMinute = calender[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            context,
            OnTimeSetListener { _, hourOfDay, minute -> inTime.setText("$hourOfDay:$minute") },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }

    override fun setAlarm(
        context: Context,
        dateString: String,
        timeString: String,
        noteTitle: String,
        noteDescription: String
    ) {
        val manager = getSystemService(context, AlarmManager::class.java) as AlarmManager
        val localDateTime = LocalDateTime.parse(
            "$dateString $timeString",
            DateTimeFormatter.ofPattern("d-M-yyyy H:m")
        )
        val alarmTime = localDateTime.plusMinutes(1)
        Log.i("Converted Date", "${alarmTime.toString()}")
        val intent = Intent(context, MyAlarm::class.java)
        intent.putExtra("noteTitle", noteTitle)
        intent.putExtra("noteDescription", noteDescription)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        Log.i("Till", "U r till here")
         manager.setRepeating(
             AlarmManager.RTC,
             localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(),
             AlarmManager.INTERVAL_DAY,
             pendingIntent
         )
    }
}