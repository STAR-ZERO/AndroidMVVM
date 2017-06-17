package com.star_zero.example.androidmvvm.infrastructure

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.star_zero.example.androidmvvm.infrastructure.converter.Converters
import com.star_zero.example.androidmvvm.infrastructure.task.TaskDao
import com.star_zero.example.androidmvvm.infrastructure.task.TaskTable

@Database(entities = arrayOf(TaskTable::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}