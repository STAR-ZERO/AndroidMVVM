package com.star_zero.example.androidmvvm.application

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.domain.task.TaskValidator
import com.star_zero.example.androidmvvm.utils.AsyncResult
import com.star_zero.example.androidmvvm.utils.PublishLiveData
import com.star_zero.example.androidmvvm.utils.extension.fire
import com.star_zero.example.androidmvvm.utils.extension.observe
import timber.log.Timber
import javax.inject.Inject

class TaskService @Inject constructor(val taskRepository: TaskRepository) {

    private val validator = TaskValidator()

    fun fetchTasks(owner: LifecycleOwner) {
        taskRepository.fetchTasks().observe(owner) {
            when (it) {
                is AsyncResult.Success -> {
                    tasks.value = it.value
                }
                is AsyncResult.Error -> {
                    Timber.w(it.error)
                    errorFetchTasks.fire()
                }
            }
        }
    }

    fun save(taskDTO: TaskDTO, owner: LifecycleOwner) {
        taskDTO.clearValidationErrors()

        if (!validator.validate(taskDTO.title, taskDTO.description)) {
            validationError.fire()
            taskDTO.setValidationErrors(validator.errors)
            return
        }

        if (taskDTO.task == null) {
            saveNewTask(taskDTO, owner)
        } else {
            updateTask(taskDTO, owner)
        }
    }

    private fun saveNewTask(taskDTO: TaskDTO, owner: LifecycleOwner) {
        val task = Task.createNewTask(taskRepository.generateTaskId(), taskDTO.title!!, taskDTO.description)

        taskRepository.save(task).observe(owner) {
            when(it) {
                is AsyncResult.Success -> successSaveTask.fire()
                is AsyncResult.Error -> {
                    Timber.w(it.error)
                    errorSaveTask.fire()
                }
            }
        }
    }

    private fun updateTask(taskDTO: TaskDTO, owner: LifecycleOwner) {
        val task = taskDTO.task!!
        task.update(taskDTO.title!!, taskDTO.description)

        taskRepository.update(task).observe(owner) {
            when(it) {
                is AsyncResult.Success -> successSaveTask.fire()
                is AsyncResult.Error -> {
                    Timber.w(it.error)
                    errorSaveTask.fire()
                }
            }
        }
    }

    fun changeCompleteState(task: Task, completed: Boolean, owner: LifecycleOwner) {
        if (completed) {
            task.completeTask()
        } else {
            task.activateTask()
        }

        taskRepository.update(task).observe(owner) {
            when(it) {
                is AsyncResult.Error -> {
                    Timber.w(it.error)
                    errorChangeCompleteState.fire()

                    // rollback
                    if (completed) {
                        task.activateTask()
                    } else {
                        task.completeTask()
                    }
                }
            }
        }
    }

    fun deleteTask(task: Task, owner: LifecycleOwner) {
        taskRepository.delete(task).observe(owner) {
            when (it) {
                is AsyncResult.Success -> successDeleteTask.fire()
                is AsyncResult.Error -> {
                    Timber.w(it.error)
                    errorDeleteTask.fire()
                }
            }
        }
    }

    // fetchTasks

    private val tasks = PublishLiveData<List<Task>?>()
    fun tasks(): LiveData<List<Task>?> = tasks
    private val errorFetchTasks = PublishLiveData<Void>()
    fun errorFetchTasks(): LiveData<Void> = errorFetchTasks

    // save

    private val successSaveTask = PublishLiveData<Void>()
    fun successSaveTask(): LiveData<Void> = successSaveTask
    private val errorSaveTask = PublishLiveData<Void>()
    fun errorSaveTask(): LiveData<Void> = errorSaveTask

    // validation

    private val validationError = PublishLiveData<Void>()
    fun validationError(): LiveData<Void> = validationError

    // completeTask, activateTask

    private val errorChangeCompleteState = PublishLiveData<Void>()
    fun errorChangeCompleteState(): LiveData<Void> = errorChangeCompleteState

    // delete

    private val successDeleteTask = PublishLiveData<Void>()
    fun successDeleteTask(): LiveData<Void> = successDeleteTask
    private val errorDeleteTask = PublishLiveData<Void>()
    fun errorDeleteTask(): LiveData<Void> = errorDeleteTask

}