package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import rx.Observable
import java.util.*

class MockTaskRepository : TaskRepository {

    override fun generateTaskId(): TaskId {
        return TaskId("id")
    }

    override fun save(task: Task): Observable<Boolean> {
        return Observable.just(true)
    }

    override fun update(task: Task): Observable<Boolean> {
        return Observable.just(true)
    }

    override fun delete(task: Task): Observable<Boolean> {
        return Observable.just(true)
    }

    override fun fetchTasks(): Observable<List<Task>> {
        val tasks = ArrayList<Task>()
        tasks.add(Task.createNewTask(TaskId("id1"), "title1", "description1"))
        tasks.add(Task.createNewTask(TaskId("id2"), "title2", "description2"))
        return Observable.just<List<Task>>(tasks)
    }
}
