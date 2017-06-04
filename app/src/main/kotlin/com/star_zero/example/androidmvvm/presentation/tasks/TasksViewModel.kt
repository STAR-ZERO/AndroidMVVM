package com.star_zero.example.androidmvvm.presentation.tasks

import android.view.View
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.application.TaskService
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.presentation.tasks.adapter.TasksAdapter
import com.star_zero.example.androidmvvm.utils.Irrelevant
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TasksViewModel @Inject constructor(val taskService: TaskService) {

    val adapter: TasksAdapter = TasksAdapter()

    private val disposables = CompositeDisposable()

    fun onCreate() {
        subscribe()
    }

    fun onStart() {
        fetchTasks()
    }

    fun onDestroy() {
        taskService.onDestroy()
        disposables.clear()
    }

    // ----------------------
    // Event
    // ----------------------

    fun onClickNewTask(@Suppress("UNUSED_PARAMETER") view: View) {
        clickNewTaskSubject.onNext(Irrelevant.INSTANCE)
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
        disposables.add(taskService.tasks.subscribe(adapter::setTasks))
        disposables.add(taskService.errorFetchTasks.subscribe {
            errorMessageSubject.onNext(R.string.error_get_task)
        })
        disposables.add(taskService.errorChangeCompleteState.subscribe {
            errorMessageSubject.onNext(R.string.error_change_state)
        })
    }

    // ----------------------
    // Notification
    // ----------------------

    private val clickNewTaskSubject = PublishSubject.create<Irrelevant>()
    val clickNewTask: Observable<Irrelevant> = clickNewTaskSubject.hide()

    private val errorMessageSubject = PublishSubject.create<Int>()
    val errorMessage: Observable<Int> = errorMessageSubject.hide()
}

