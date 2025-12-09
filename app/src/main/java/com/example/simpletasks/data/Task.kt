package com.example.simpletasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),

    // НОВО
    val dueDate: Long? = null,       // крайно време в millis (може да е null)
    val isDone: Boolean = false,     // задача изпълнена ли е
    val priority: Int = 0            // 0 = low, 1 = normal, 2 = high
)
