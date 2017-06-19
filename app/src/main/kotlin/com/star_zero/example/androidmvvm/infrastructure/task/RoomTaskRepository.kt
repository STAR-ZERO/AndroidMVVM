package com.star_zero.example.androidmvvm.infrastructure.task

import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.infrastructure.AppDatabase
import com.star_zero.example.androidmvvm.utils.AsyncLiveData
import java.util.*

class RoomTaskRepository(val db: AppDatabase) : TaskRepository {

    override fun generateTaskId(): TaskId {
        return TaskId(UUID.randomUUID().toString())
    }

    override fun save(task: Task): AsyncLiveData<Unit> {
        return AsyncLiveData.create {
            db.taskDao().insertTask(TaskTable.createFromTask(task))
        }
    }

    override fun update(task: Task): AsyncLiveData<Unit> {
        return AsyncLiveData.create {
            db.taskDao().updateTask(TaskTable.createFromTask(task))
        }
    }

    override fun delete(task: Task): AsyncLiveData<Unit> {
        return AsyncLiveData.create {
            db.taskDao().deleteTasks(TaskTable.createFromTask(task))
        }
    }

    override fun fetchTasks(): AsyncLiveData<List<Task>> {
        return AsyncLiveData.create {
            val taskTables = db.taskDao().fetchTask()
            taskTables.map { it.toTask() }
        }
    }
}