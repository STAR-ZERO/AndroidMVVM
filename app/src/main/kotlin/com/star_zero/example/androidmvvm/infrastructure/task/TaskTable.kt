package com.star_zero.example.androidmvvm.infrastructure.task

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import org.joda.time.DateTime

@Entity(tableName = "task")
data class TaskTable(
        @PrimaryKey
        var taskId: TaskId,
        var title: String,
        var description: String?,
        var completed: Boolean,
        var createdAt: DateTime) {

    companion object {
        fun createFromTask(task: Task): TaskTable {
            return TaskTable(
                    task.id,
                    task.title,
                    task.description,
                    task.completed,
                    task.createdAt
            )
        }
    }

    fun toTask(): Task {
        @Suppress("DEPRECATION")
        return Task.createFromPersistent(
                taskId,
                title,
                description,
                completed,
                createdAt
        )
    }
}
