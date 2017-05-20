package com.star_zero.example.androidmvvm.application

import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.domain.task.TaskValidator
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class TaskService @Inject constructor(val taskRepository: TaskRepository) {

    private val subscriptions = CompositeSubscription()

    private val validator = TaskValidator()

    fun fetchTasks() {
        subscriptions.add(taskRepository.fetchTasks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Task>> {
                    override fun onNext(tasks: List<Task>?) {
                        tasksSubject.onNext(tasks)
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorFetchTasksSubject.onNext(null)
                    }

                    override fun onCompleted() {
                    }
                })
        )
    }

    fun save(taskDTO: TaskDTO) {
        taskDTO.clearValidationErrors()

        if (!validator.validate(taskDTO.title, taskDTO.description)) {
            validationErrorSubject.onNext(null)
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

        subscriptions.add(taskRepository.save(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successSaveTaskSubject.onNext(null)
                        } else {
                            errorSaveTaskSubject.onNext(null)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorSaveTaskSubject.onNext(null)
                    }

                    override fun onCompleted() {
                    }
                })
        )
    }

    private fun updateTask(taskDTO: TaskDTO) {
        val task = taskDTO.task!!
        task.update(taskDTO.title!!, taskDTO.description)

        subscriptions.add(taskRepository.update(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successSaveTaskSubject.onNext(null)
                        } else {
                            errorSaveTaskSubject.onNext(null)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorSaveTaskSubject.onNext(null)
                    }

                    override fun onCompleted() {
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

        subscriptions.add(taskRepository.update(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successDeleteTaskSubject.onNext(null)
                        } else {
                            errorSaveTaskSubject.onNext(null)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorChangeCompleteStateSubject.onNext(null)

                        // rollback
                        if (completed) {
                            task.activateTask()
                        } else {
                            task.completeTask()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
        )
    }

    fun deleteTask(task: Task) {
        subscriptions.add(taskRepository.delete(task)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successDeleteTaskSubject.onNext(null)
                        } else {
                            errorDeleteTaskSubject.onNext(null)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorDeleteTaskSubject.onNext(null)
                    }

                    override fun onCompleted() {
                    }
                })
        )
    }

    fun onDestroy() {
        subscriptions.clear()
    }

    // fetchTasks
    private val tasksSubject = PublishSubject.create<List<Task>>()
    val tasks: Observable<List<Task>> = tasksSubject.asObservable()
    private val errorFetchTasksSubject = PublishSubject.create<Void>()
    val errorFetchTasks: Observable<Void> = errorFetchTasksSubject.asObservable()

    // save
    private val successSaveTaskSubject = PublishSubject.create<Void>()
    val successSaveTask: Observable<Void> = successSaveTaskSubject.asObservable()
    private val errorSaveTaskSubject = PublishSubject.create<Void>()
    val errorSaveTask: Observable<Void> = errorSaveTaskSubject.asObservable()

    // validation
    private val validationErrorSubject = PublishSubject.create<Void>()
    val validationError: Observable<Void> = validationErrorSubject.asObservable()

    // completeTask, activateTask
    private val errorChangeCompleteStateSubject = PublishSubject.create<Void>()
    val errorChangeCompleteState: Observable<Void> = errorChangeCompleteStateSubject.asObservable()

    // delete
    private val successDeleteTaskSubject = PublishSubject.create<Void>()
    val successDeleteTask: Observable<Void> = successDeleteTaskSubject.asObservable()
    private val errorDeleteTaskSubject = PublishSubject.create<Void>()
    val errorDeleteTask: Observable<Void> = errorDeleteTaskSubject.asObservable()

}