package com.example.simpletasks.data

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun getActiveTasks(): LiveData<List<Task>> = taskDao.getActiveTasks()

    fun getCompletedTasks(): LiveData<List<Task>> = taskDao.getCompletedTasks()

    fun searchTasks(query: String): LiveData<List<Task>> =
        taskDao.searchTasks(query)

    // 👇 точно това ти липсваше – ползваме getById от DAO
    suspend fun getTaskById(id: Long): Task? =
        taskDao.getById(id)

    suspend fun insert(task: Task): Long =
        taskDao.insert(task)

    suspend fun update(task: Task) =
        taskDao.update(task)

    suspend fun delete(task: Task) =
        taskDao.delete(task)
}
