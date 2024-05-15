package com.aman.todoapp.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aman.todoapp.R
import com.aman.todoapp.db.DBHelper
import com.aman.todoapp.model.Task
import com.aman.todoapp.ui.AddTaskActivity
import com.aman.todoapp.ui.TaskDetailActivity

class TaskAdapter(
    private val context: Context,
    private var tasks: ArrayList<Task>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun refreshTasks(newTasks: ArrayList<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val deleteBtn: ImageView = itemView.findViewById(R.id.deleteButton)
        private val editBtn: ImageView = itemView.findViewById(R.id.editButton)

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = trimDescription(task.description)
            deleteBtn.setOnClickListener {
                showDeleteConfirmationDialog(task)
            }
            editBtn.setOnClickListener {
                navigateToAddTaskActivity(task.id!!)
            }
            itemView.setOnClickListener {
                navigateToTaskDetailActivity(task.id!!)
            }
        }

        private fun navigateToAddTaskActivity(taskId: Int) {
            val intent = Intent(context, AddTaskActivity::class.java)
            intent.putExtra("taskId", taskId)
            context.startActivity(intent)
        }

        private fun navigateToTaskDetailActivity(taskId: Int) {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra("taskId", taskId)
            context.startActivity(intent)
        }

        private fun trimDescription(description: String): String {
            val words = description.split("\\s+".toRegex())
            val trimmedDescription = words.take(10).joinToString(" ")
            return if (words.size > 10) "$trimmedDescription..." else trimmedDescription
        }

    }

    private fun showDeleteConfirmationDialog(task: Task) {
        AlertDialog.Builder(context)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteTask(task)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deleteTask(task: Task) {
        val dbHelper = DBHelper(context)
        dbHelper.deleteTask(task.id!!)
        removeItemById(task.id)
    }

    private fun removeItem(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun removeItemById(taskId: Int) {
        val position = tasks.indexOfFirst { it.id == taskId }
        if (position != -1) {
            removeItem(position)
        }
    }
}
