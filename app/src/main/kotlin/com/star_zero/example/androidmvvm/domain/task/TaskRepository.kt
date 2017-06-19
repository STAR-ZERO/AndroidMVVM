package com.star_zero.example.androidmvvm.domain.task

import com.star_zero.example.androidmvvm.utils.AsyncLiveData

interface TaskRepository {

    fun generateTaskId(): TaskId

    fun save(task: Task): AsyncLiveData<Unit>

    fun update(task: Task): AsyncLiveData<Unit>

    fun delete(task: Task): AsyncLiveData<Unit>

    fun fetchTasks(): AsyncLiveData<List<Task>>
}