package com.star_zero.example.androidmvvm.presentation.tasks

import android.view.View
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.application.TaskService
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.presentation.tasks.adapter.TasksAdapter
import rx.Observable
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class TasksViewModel @Inject constructor(val taskService: TaskService) {

    val adapter: TasksAdapter = TasksAdapter()

    private val subscriptions = CompositeSubscription()

    fun onCreate() {
        subscribe()
    }

    fun onStart() {
        fetchTasks()
    }

    fun onDestroy() {
        taskService.onDestroy()
        subscriptions.clear()
    }

    // ----------------------
    // Event
    // ----------------------

    fun onClickNewTask(@Suppress("UNUSED_PARAMETER") view: View) {
        clickNewTaskSubject.onNext(null)
    }

    // ----------------------
    // Operation
    // ----------------------

    private fun fetchTasks() {
        taskService.fetchTasks()
    }

    fun changeCompleteState(task: Task, completed: Boolean) {
        taskService.changeCompleteState(task, completed)
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private fun subscribe() {
        subscriptions.add(taskService.tasks.subscribe(adapter::setTasks))
        subscriptions.add(taskService.errorFetchTasks.subscribe {
            errorMessageSubject.onNext(R.string.error_get_task)
        })
        subscriptions.add(taskService.errorChangeCompleteState.subscribe {
            errorMessageSubject.onNext(R.string.error_change_state)
        })
    }

    // ----------------------
    // Notification
    // ----------------------

    private val clickNewTaskSubject = PublishSubject.create<Void>()
    val clickNewTask: Observable<Void> = clickNewTaskSubject.asObservable()

    private val errorMessageSubject = PublishSubject.create<Int>()
    val errorMessage: Observable<Int> = errorMessageSubject.asObservable()
}

