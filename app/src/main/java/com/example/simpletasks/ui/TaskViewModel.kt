package com.example.simpletasks.ui

import androidx.lifecycle.*
import com.example.simpletasks.data.Task
import com.example.simpletasks.data.TaskRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.switchMap


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // 🔹 източник за списъка със задачи (различни заявки: всички, активни, завършени, търсене)
    private val _tasksSource = MutableLiveData<LiveData<List<Task>>>()
    val tasks: LiveData<List<Task>> = _tasksSource.switchMap { it }


    // 🔹 текущо избрана задача за екрана с детайли
    private val _selectedTask = MutableLiveData<Task?>()
    val selectedTask: LiveData<Task?> = _selectedTask

    init {
        loadAllTasks()
    }

    // 👉 Списък – всички задачи
    fun loadAllTasks() {
        _tasksSource.value = repository.getAllTasks()
    }

    // 👉 само активни
    fun loadActiveTasks() {
        _tasksSource.value = repository.getActiveTasks()
    }

    // 👉 само завършени
    fun loadCompletedTasks() {
        _tasksSource.value = repository.getCompletedTasks()
    }

    // 👉 търсене по заглавие
    fun searchTasks(query: String) {
        if (query.isBlank()) {
            loadAllTasks()
        } else {
            _tasksSource.value = repository.searchTasks(query)
        }
    }

    // 👉 зареждане на задача по id за детайлния екран
    fun loadTask(id: Long) {
        viewModelScope.launch {
            val task = repository.getTaskById(id)   // suspend → в корутина
            _selectedTask.postValue(task)
        }
    }

    // 👉 изчистване на избраната задача (когато създаваме нова)
    fun clearSelectedTask() {
        _selectedTask.value = null
    }

    // 👉 запис (create/update) със валидация + callbacks за Toast-ове
    fun saveTask(
        id: Long?,
        title: String,
        description: String,
        isDone: Boolean,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (title.isBlank()) {
            onError("Заглавието не може да е празно")
            return
        }

        viewModelScope.launch {
            val task = if (id == null) {
                Task(
                    title = title,
                    description = description,
                    isDone = isDone
                )
            } else {
                Task(
                    id = id,
                    title = title,
                    description = description,
                    isDone = isDone
                )
            }

            if (id == null) {
                repository.insert(task)
            } else {
                repository.update(task)
            }

            onSuccess()
        }
    }

    // 👉 изтриване
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    // 👉 toggle на isDone (ако решиш да го ползваш някъде)
    fun toggleDone(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isDone = !task.isDone))
        }
    }
}
