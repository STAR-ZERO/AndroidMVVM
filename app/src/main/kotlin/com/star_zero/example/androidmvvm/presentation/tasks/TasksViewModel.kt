package com.star_zero.example.androidmvvm.presentation.tasks

import android.arch.lifecycle.*
import android.view.View
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.application.TaskService
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.presentation.shared.viewmodel.ViewModelObservable
import com.star_zero.example.androidmvvm.utils.PublishLiveData
import com.star_zero.example.androidmvvm.utils.extension.fire
import com.star_zero.example.androidmvvm.utils.extension.observe
import javax.inject.Inject

class TasksViewModel @Inject constructor(val taskService: TaskService) : ViewModelObservable(), LifecycleObserver {

    private var owner: LifecycleOwner? = null

    // ----------------------
    // Lifecycle
    // ----------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
        subscribe(owner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        fetchTasks()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        owner = null
    }

    // ----------------------
    // Event
    // ----------------------

    fun onClickNewTask(@Suppress("UNUSED_PARAMETER") view: View) {
        clickNewTask.fire()
    }

    // ----------------------
    // Operation
    // ----------------------

    private fun fetchTasks() {
        taskService.fetchTasks(owner!!)
    }

    fun changeCompleteState(task: Task, completed: Boolean) {
        taskService.changeCompleteState(task, completed, owner!!)
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private fun subscribe(owner: LifecycleOwner) {
        taskService.errorFetchTasks().observe(owner) {
            errorMessage.value = R.string.error_get_task
        }

        taskService.errorFetchTasks().observe(owner) {
            errorMessage.value = R.string.error_get_task
        }

        taskService.errorChangeCompleteState().observe(owner) {
            errorMessage.value = R.string.error_change_state
        }
    }

    // ----------------------
    // LiveData
    // ----------------------

    fun tasks(): LiveData<List<Task>> = Transformations.map(taskService.tasks(), { it })

    private val clickNewTask = PublishLiveData<Void>()
    fun clickNewTask(): LiveData<Void> = clickNewTask

    private val errorMessage = PublishLiveData<Int>()
    fun errorMessage(): LiveData<Int> = errorMessage
}

