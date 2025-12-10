package com.example.simpletasks.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Query(
        """
        SELECT * FROM tasks
        ORDER BY 
            isDone ASC,                  -- първо активните
            priority DESC,               -- после по приоритет
            CASE WHEN dueDate IS NULL THEN 1 ELSE 0 END, -- задачите с дата отгоре
            dueDate ASC
        """
    )
    fun getAllTasks(): LiveData<List<Task>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE isDone = 0
        ORDER BY 
            priority DESC,
            CASE WHEN dueDate IS NULL THEN 1 ELSE 0 END,
            dueDate ASC
        """
    )
    fun getActiveTasks(): LiveData<List<Task>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE isDone = 1
        ORDER BY dueDate ASC
        """
    )
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE title LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
        """
    )
    fun searchTasks(query: String): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): Task?
}
