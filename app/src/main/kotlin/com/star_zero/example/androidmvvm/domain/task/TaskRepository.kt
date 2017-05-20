package com.star_zero.example.androidmvvm.domain.task

import rx.Observable

interface TaskRepository {

    fun generateTaskId(): TaskId

    fun save(task: Task): Observable<Boolean>

    fun update(task: Task): Observable<Boolean>

    fun delete(task: Task): Observable<Boolean>

    fun fetchTasks(): Observable<List<Task>>
}