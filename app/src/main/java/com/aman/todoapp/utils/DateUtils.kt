package com.aman.todoapp.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateUtils {

    fun formatDate(date: Date, dateFormat: SimpleDateFormat): String {
        return dateFormat.format(date)
    }

    fun showDatePickerDialog(context: Context, onDateSetListener: (Date) -> Unit) {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, monthOfYear)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val selectedDate = selectedCalendar.time

                onDateSetListener(selectedDate)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
