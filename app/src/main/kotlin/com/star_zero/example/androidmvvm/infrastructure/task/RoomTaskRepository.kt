package com.star_zero.example.androidmvvm.infrastructure.task

import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.infrastructure.AppDatabase
import io.reactivex.Observable
import java.util.*

class RoomTaskRepository(val db: AppDatabase) : TaskRepository {

    override fun generateTaskId(): TaskId {
        return TaskId(UUID.randomUUID().toString())
    }

    override fun save(task: Task): Observable<Boolean> {
        return Observable.create({ emitter ->
            try {
                db.taskDao().insertTask(TaskTable.createFromTask(task))
                emitter.onNext(true)
                emitter.onComplete()

            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

    override fun update(task: Task): Observable<Boolean> {
        return Observable.create({ emitter ->
            try {
                db.taskDao().updateTask(TaskTable.createFromTask(task))
                emitter.onNext(true)
                emitter.onComplete()

            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

    override fun delete(task: Task): Observable<Boolean> {
        return Observable.create({ emitter ->
            try {
                db.taskDao().deleteTasks(TaskTable.createFromTask(task))
                emitter.onNext(true)
                emitter.onComplete()

            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

    override fun fetchTasks(): Observable<List<Task>> {
        return Observable.create({ emitter ->
            try {
                val taskTables = db.taskDao().fetchTask()
                val tasks = taskTables.map { it.toTask() }
                emitter.onNext(tasks)
                emitter.onComplete()

            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }
}