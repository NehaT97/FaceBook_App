package com.bridgelabz.fundooapplication.model

import android.content.Context
import android.widget.EditText

interface IReminderService {
    fun selectDate(context: Context, inDate: EditText)
    fun selectTime(context: Context, inTime: EditText)

}