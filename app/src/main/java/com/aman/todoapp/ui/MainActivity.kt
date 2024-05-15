package com.aman.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aman.todoapp.R
import com.aman.todoapp.db.DBHelper
import com.aman.todoapp.utils.DateUtils
import com.aman.todoapp.utils.TaskAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var selectedDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedDate = Calendar.getInstance().time

        setupRecyclerView()
        setupSelectDateButton()
        setupAddNewTaskButton()
    }

    override fun onResume() {
        super.onResume()
        refreshTasks()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(this, ArrayList())
        recyclerView.adapter = taskAdapter
    }

    private fun setupSelectDateButton() {
        val selectDateButton: Button = findViewById(R.id.selectDateButton)
        updateSelectDateButtonText()
        selectDateButton.setOnClickListener {
            DateUtils.showDatePickerDialog(this) { selectedDate ->
                this.selectedDate = selectedDate
                updateSelectDateButtonText()
                refreshTasks()
            }
        }
    }

    private fun setupAddNewTaskButton() {
        val addNewTask: Button = findViewById(R.id.gotoAddTaskBtn)
        addNewTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    private fun updateSelectDateButtonText() {
        val selectDateButton: Button = findViewById(R.id.selectDateButton)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        selectDateButton.text = DateUtils.formatDate(selectedDate, dateFormat)
    }

    private fun refreshTasks() {
        val tasks = DBHelper(this).getTasksByDate(selectedDate)
        taskAdapter.refreshTasks(tasks)
    }
}