package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import io.reactivex.Observable

abstract class MockDefaultTaskRepository : TaskRepository {

    override fun generateTaskId(): TaskId {
        return TaskId("")
    }

    override fun save(task: Task): Observable<Boolean> {
        return Observable.just(false)
    }

    override fun update(task: Task): Observable<Boolean> {
        return Observable.just(false)
    }

    override fun delete(task: Task): Observable<Boolean> {
        return Observable.just(false)
    }

    override fun fetchTasks(): Observable<List<Task>> {
        return Observable.just(listOf())
    }
}
