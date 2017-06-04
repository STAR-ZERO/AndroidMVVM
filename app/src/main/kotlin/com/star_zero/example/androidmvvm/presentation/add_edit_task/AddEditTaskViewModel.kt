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
import com.star_zero.example.androidmvvm.utils.Irrelevant
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddEditTaskViewModel @Inject constructor(private val taskService: TaskService) : BaseObservable() {

    private val STATE_KEY_TASK = "task"

    private val disposables = CompositeDisposable()

    fun onCreate() {
        subscribe()
    }

    fun onDestroy() {
        taskService.onDestroy()
        disposables.clear()
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
        disposables.add(taskService.validationError.subscribe {
            errorMessageSubject.onNext(R.string.error_validation)
        })
        disposables.add(taskService.successSaveTask.subscribe(successSaveTaskSubject::onNext))
        disposables.add(taskService.errorSaveTask.subscribe {
            errorMessageSubject.onNext(R.string.error_save_task)
        })

        // delete
        disposables.add(taskService.successDeleteTask.subscribe(successDeleteTaskSubject::onNext))
        disposables.add(taskService.errorDeleteTask.subscribe {
            errorMessageSubject.onNext(R.string.error_delete_task)
        })
    }

    // ----------------------
    // Notification
    // ----------------------

    private val errorMessageSubject = PublishSubject.create<Int>()
    val errorMessage: Observable<Int> = errorMessageSubject.hide()

    private val successSaveTaskSubject = PublishSubject.create<Irrelevant>()
    val successSaveTask: Observable<Irrelevant> = successSaveTaskSubject.hide()

    private val successDeleteTaskSubject = PublishSubject.create<Irrelevant>()
    val successDeleteTask: Observable<Irrelevant> = successDeleteTaskSubject.hide()
}
