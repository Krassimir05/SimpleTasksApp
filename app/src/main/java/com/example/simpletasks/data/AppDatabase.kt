package com.example.simpletasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class],
    version = 2,        // ↩ ако беше 1, увеличи на 2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "simple_tasks_db"
                )
                    .fallbackToDestructiveMigration() // ако структурата се промени => чисто начало
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
