package com.star_zero.example.androidmvvm.application

import android.arch.lifecycle.LiveData
import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.domain.task.TaskValidator
import com.star_zero.example.androidmvvm.utils.PublishLiveData
import com.star_zero.example.androidmvvm.utils.extension.fire
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TaskService @Inject constructor(val taskRepository: TaskRepository) {

    private val disposables = CompositeDisposable()

    private val validator = TaskValidator()

    fun fetchTasks() {
        disposables.add(taskRepository.fetchTasks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Task>>() {
                    override fun onNext(tasks: List<Task>?) {
                        this@TaskService.tasks.value = tasks
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorFetchTasks.fire()
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    fun save(taskDTO: TaskDTO) {
        taskDTO.clearValidationErrors()

        if (!validator.validate(taskDTO.title, taskDTO.description)) {
            validationError.fire()
            taskDTO.setValidationErrors(validator.errors)
            return
        }

        if (taskDTO.task == null) {
            saveNewTask(taskDTO)
        } else {
            updateTask(taskDTO)
        }
    }

    private fun saveNewTask(taskDTO: TaskDTO) {
        val task = Task.createNewTask(taskRepository.generateTaskId(), taskDTO.title!!, taskDTO.description)

        disposables.add(taskRepository.save(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onNext(result: Boolean) {
                        if (result) {
                            successSaveTask.fire()
                        } else {
                            errorSaveTask.fire()
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorSaveTask.fire()
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    private fun updateTask(taskDTO: TaskDTO) {
        val task = taskDTO.task!!
        task.update(taskDTO.title!!, taskDTO.description)

        disposables.add(taskRepository.update(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onNext(result: Boolean) {
                        if (result) {
                            successSaveTask.fire()
                        } else {
                            errorSaveTask.fire()
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorSaveTask.fire()
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    fun changeCompleteState(task: Task, completed: Boolean) {
        if (completed) {
            task.completeTask()
        } else {
            task.activateTask()
        }

        disposables.add(taskRepository.update(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onNext(result: Boolean?) {
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorChangeCompleteState.fire()

                        // rollback
                        if (completed) {
                            task.activateTask()
                        } else {
                            task.completeTask()
                        }
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    fun deleteTask(task: Task) {
        disposables.add(taskRepository.delete(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onNext(result: Boolean) {
                        if (result) {
                            successDeleteTask.fire()
                        } else {
                            errorDeleteTask.fire()
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorDeleteTask.fire()
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    fun onDestroy() {
        disposables.clear()
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