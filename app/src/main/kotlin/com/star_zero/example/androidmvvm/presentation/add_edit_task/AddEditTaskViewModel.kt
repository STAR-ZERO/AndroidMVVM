package com.star_zero.example.androidmvvm.presentation.add_edit_task

import android.arch.lifecycle.*
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.application.TaskService
import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.presentation.shared.viewmodel.ViewModelObservable
import com.star_zero.example.androidmvvm.utils.PublishLiveData
import com.star_zero.example.androidmvvm.utils.extension.fire
import com.star_zero.example.androidmvvm.utils.extension.observe
import javax.inject.Inject

class AddEditTaskViewModel @Inject constructor(private val taskService: TaskService) : ViewModelObservable(), LifecycleObserver {

    // ----------------------
    // Lifecycle
    // ----------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner) {
        subscribe(owner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        taskService.onDestroy()
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

    private fun subscribe(owner: LifecycleOwner) {
        // save
        taskService.validationError().observe(owner) {
            errorMessage.value = R.string.error_validation
        }
        taskService.successSaveTask().observe(owner) {
            successSaveTask.fire()
        }
        taskService.errorSaveTask().observe(owner) {
            errorMessage.value = R.string.error_save_task
        }

        // delete
        taskService.successDeleteTask().observe(owner) {
            successDeleteTask.fire()
        }
        taskService.errorDeleteTask().observe(owner) {
            errorMessage.value = R.string.error_delete_task
        }
    }

    // ----------------------
    // LiveData
    // ----------------------

    private val errorMessage = PublishLiveData<Int>()
    fun errorMessage(): LiveData<Int> = errorMessage

    private val successSaveTask = PublishLiveData<Void>()
    fun successSaveTask(): LiveData<Void> = successSaveTask

    private val successDeleteTask = PublishLiveData<Void>()
    fun successDeleteTask(): LiveData<Void> = successDeleteTask
}
