package com.star_zero.example.androidmvvm.presentation.add_edit_task

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Bundle
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.application.TaskService
import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.domain.task.Task
import rx.Observable
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class AddEditTaskViewModel @Inject constructor(private val taskService: TaskService) : BaseObservable() {

    private val STATE_KEY_TASK = "task"

    private val subscriptions = CompositeSubscription()

    fun onCreate() {
        subscribe()
    }

    fun onDestroy() {
        taskService.onDestroy()
        subscriptions.clear()
    }

    fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return
        taskDTO = savedInstanceState.getParcelable<TaskDTO>(STATE_KEY_TASK)
    }

    fun saveState(outState: Bundle) {
        outState.putParcelable(STATE_KEY_TASK, taskDTO)
    }

    // ----------------------
    // Binding
    // ----------------------

    @get:Bindable
    var taskDTO: TaskDTO = TaskDTO()
        private set(taskDTO) {
            field = taskDTO
            notifyPropertyChanged(BR.taskDTO)
        }

    // ----------------------
    // Property
    // ----------------------

    fun setTask(task: Task) {
        this.taskDTO = TaskDTO.createFromTask(task)
    }

    val isSaved: Boolean
        get() = taskDTO.task != null

    // ----------------------
    // Event
    // ----------------------

    fun onClickSave(@Suppress("UNUSED_PARAMETER") view: View) {
        saveTask()
    }

    // ----------------------
    // operation
    // ----------------------

    private fun saveTask() {
        taskService.save(taskDTO)
    }

    fun deleteTask() {
        val task = this.taskDTO.task ?: return
        taskService.deleteTask(task)
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private fun subscribe() {
        // save
        subscriptions.add(taskService.validationError.subscribe {
            errorMessageSubject.onNext(R.string.error_validation)
        })
        subscriptions.add(taskService.successSaveTask.subscribe(successSaveTaskSubject::onNext))
        subscriptions.add(taskService.errorSaveTask.subscribe {
            errorMessageSubject.onNext(R.string.error_save_task)
        })

        // delete
        subscriptions.add(taskService.successDeleteTask.subscribe(successDeleteTaskSubject::onNext))
        subscriptions.add(taskService.errorDeleteTask.subscribe {
            errorMessageSubject.onNext(R.string.error_delete_task)
        })
    }

    // ----------------------
    // Notification
    // ----------------------

    private val errorMessageSubject = PublishSubject.create<Int>()
    val errorMessage: Observable<Int> = errorMessageSubject.asObservable()

    private val successSaveTaskSubject = PublishSubject.create<Void>()
    val successSaveTask: Observable<Void> = successSaveTaskSubject.asObservable()

    private val successDeleteTaskSubject = PublishSubject.create<Void>()
    val successDeleteTask: Observable<Void> = successDeleteTaskSubject.asObservable()!!
}
