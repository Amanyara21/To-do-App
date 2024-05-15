package com.aman.todoapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aman.todoapp.R
import com.aman.todoapp.db.DBHelper
import com.aman.todoapp.model.Task
import com.aman.todoapp.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var addTaskButton: Button

    private var taskId: Int? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dbHelper = DBHelper(this)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        dateEditText = findViewById(R.id.dateEditText)
        addTaskButton = findViewById(R.id.addTaskButton)

        taskId = intent.getIntExtra("taskId", -1)
        if (taskId != -1) {
            val task = dbHelper.getTask(taskId!!)
            titleEditText.setText(task?.title)
            descriptionEditText.setText(task?.description)
            val formattedDate = task?.date?.let { DateUtils.formatDate(it, dateFormat) }
            dateEditText.setText(formattedDate)
            addTaskButton.text = "Update Task"
        }

        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        addTaskButton.setOnClickListener {
            if (taskId != -1) {
                updateTask()
            } else {
                addTask()
            }
        }
    }

    private fun showDatePickerDialog() {
        DateUtils.showDatePickerDialog(this) { selectedDate ->
            val formattedDate = DateUtils.formatDate(selectedDate, dateFormat)
            dateEditText.setText(formattedDate)
        }
    }

    private fun addTask() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val dateString = dateEditText.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || dateString.isEmpty()) {
            showToast("Please fill all fields")
            return
        }

        val date: Date = dateFormat.parse(dateString)!!

        val task = Task(null, title, description, date)
        dbHelper.addTask(task)

        showToast("Task added successfully")
        emptyAllFields()
    }

    private fun updateTask() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val dateString = dateEditText.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || dateString.isEmpty()) {
            showToast("Please fill all fields")
            return
        }
        val date: Date = dateFormat.parse(dateString)!!
        val task = Task(taskId, title, description, date)
        dbHelper.updateTask(task)

        showToast("Task updated successfully")
        emptyAllFields()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private fun emptyAllFields(){
        titleEditText.setText("")
        descriptionEditText.setText("")
        dateEditText.setText("")
    }
}

