package com.star_zero.example.androidmvvm.application

import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.domain.task.TaskValidator
import com.star_zero.example.androidmvvm.utils.Irrelevant
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
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
                        tasksSubject.onNext(tasks)
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorFetchTasksSubject.onNext(Irrelevant.INSTANCE)
                    }

                    override fun onComplete() {
                    }
                })
        )
    }

    fun save(taskDTO: TaskDTO) {
        taskDTO.clearValidationErrors()

        if (!validator.validate(taskDTO.title, taskDTO.description)) {
            validationErrorSubject.onNext(Irrelevant.INSTANCE)
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
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successSaveTaskSubject.onNext(Irrelevant.INSTANCE)
                        } else {
                            errorSaveTaskSubject.onNext(Irrelevant.INSTANCE)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorSaveTaskSubject.onNext(Irrelevant.INSTANCE)
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
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successSaveTaskSubject.onNext(Irrelevant.INSTANCE)
                        } else {
                            errorSaveTaskSubject.onNext(Irrelevant.INSTANCE)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorSaveTaskSubject.onNext(Irrelevant.INSTANCE)
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
                        if (result!!) {
                            successDeleteTaskSubject.onNext(Irrelevant.INSTANCE)
                        } else {
                            errorSaveTaskSubject.onNext(Irrelevant.INSTANCE)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorChangeCompleteStateSubject.onNext(Irrelevant.INSTANCE)

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
                    override fun onNext(result: Boolean?) {
                        if (result!!) {
                            successDeleteTaskSubject.onNext(Irrelevant.INSTANCE)
                        } else {
                            errorDeleteTaskSubject.onNext(Irrelevant.INSTANCE)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Timber.w(e)
                        errorDeleteTaskSubject.onNext(Irrelevant.INSTANCE)
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
    private val tasksSubject = PublishSubject.create<List<Task>>()
    val tasks: Observable<List<Task>> = tasksSubject.hide()
    private val errorFetchTasksSubject = PublishSubject.create<Irrelevant>()
    val errorFetchTasks: Observable<Irrelevant> = errorFetchTasksSubject.hide()

    // save
    private val successSaveTaskSubject = PublishSubject.create<Irrelevant>()
    val successSaveTask: Observable<Irrelevant> = successSaveTaskSubject.hide()
    private val errorSaveTaskSubject = PublishSubject.create<Irrelevant>()
    val errorSaveTask: Observable<Irrelevant> = errorSaveTaskSubject.hide()

    // validation
    private val validationErrorSubject = PublishSubject.create<Irrelevant>()
    val validationError: Observable<Irrelevant> = validationErrorSubject.hide()

    // completeTask, activateTask
    private val errorChangeCompleteStateSubject = PublishSubject.create<Irrelevant>()
    val errorChangeCompleteState: Observable<Irrelevant> = errorChangeCompleteStateSubject.hide()

    // delete
    private val successDeleteTaskSubject = PublishSubject.create<Irrelevant>()
    val successDeleteTask: Observable<Irrelevant> = successDeleteTaskSubject.hide()
    private val errorDeleteTaskSubject = PublishSubject.create<Irrelevant>()
    val errorDeleteTask: Observable<Irrelevant> = errorDeleteTaskSubject.hide()

}