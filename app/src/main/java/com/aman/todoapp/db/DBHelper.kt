package com.aman.todoapp.db


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aman.todoapp.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Task.db"

        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"

        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_DATE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(task: Task): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            if (task.id != null) {
                put(COLUMN_ID, task.id)
            }
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DATE, DATE_FORMAT.format(task.date))
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        return newRowId != -1L
    }

    fun getTasksByDate(date: Date): ArrayList<Task> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_DATE = ?",
            arrayOf(DATE_FORMAT.format(date)),
            null,
            null,
            null
        )
        val tasks = ArrayList<Task>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val taskDate = DATE_FORMAT.parse(getString(getColumnIndexOrThrow(COLUMN_DATE)))
                tasks.add(Task(id, name, description, taskDate!!))
            }
        }
        return tasks
    }

    fun updateTask(task: Task): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DATE, DATE_FORMAT.format(task.date))
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getTask(id: Int): Task? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        var task: Task? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val date =
                DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)))
            task = Task(id, name, description, date!!)
        }
        cursor.close()
        return task
    }

}