package com.star_zero.example.androidmvvm.infrastructure.task

import android.arch.persistence.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY createdAt")
    fun fetchTask(): List<TaskTable>

    @Insert
    fun insertTask(vararg taskTable: TaskTable)

    @Update
    fun updateTask(vararg taskTable: TaskTable)

    @Delete
    fun deleteTasks(vararg taskTable: TaskTable)
}