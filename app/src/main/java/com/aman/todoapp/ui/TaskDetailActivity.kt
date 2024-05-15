package com.aman.todoapp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aman.todoapp.R
import com.aman.todoapp.db.DBHelper
import com.aman.todoapp.model.Task
import com.aman.todoapp.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var dateTextView: TextView

    private lateinit var dbHelper: DBHelper
    private var taskId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        dateTextView = findViewById(R.id.dateTextView)

        dbHelper = DBHelper(this)

        taskId = intent.getIntExtra("taskId", -1)

        val task = dbHelper.getTask(taskId!!)
        task?.let { displayTaskDetails(it) }

    }

    private fun displayTaskDetails(task: Task) {
        titleTextView.text = "Title: ${task.title}"
        descriptionTextView.text = "Description: ${task.description}"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = task.date.let { DateUtils.formatDate(it, dateFormat) }
        dateTextView.text = "Date: $formattedDate"
    }

}
