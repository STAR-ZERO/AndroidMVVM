package com.star_zero.example.androidmvvm.application;

import com.star_zero.example.androidmvvm.application.dto.TaskDTO;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskRepository;
import com.star_zero.example.androidmvvm.domain.task.TaskValidator;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class TaskService {

    private TaskRepository taskRepository;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    private TaskValidator validator = new TaskValidator();

    @Inject
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void getTasks() {
        subscriptions.add(taskRepository.getTasks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Task>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        errorGetTasksSubject.onNext(null);
                    }

                    @Override
                    public void onNext(List<Task> tasks) {
                        tasksSubject.onNext(tasks);
                    }
                })
        );
    }

    public void save(TaskDTO taskDTO) {
        taskDTO.clearValidationErrors();

        if (!validator.validate(taskDTO.getTitle(), taskDTO.getDescription())) {
            validationErrorSubject.onNext(null);
            taskDTO.setValidationErrors(validator.getErrors());
            return;
        }

        if (taskDTO.getTask() == null) {
            saveNewTask(taskDTO);
        } else {
            updateTask(taskDTO);
        }
    }

    private void saveNewTask(TaskDTO taskDTO) {

        Task task = Task.createNewTask(taskRepository.generateTaskId(), taskDTO.getTitle(), taskDTO.getDescription());

        subscriptions.add(taskRepository.save(task)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        errorSaveTaskSubject.onNext(null);
                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            successSaveTaskSubject.onNext(null);
                        } else {
                            errorSaveTaskSubject.onNext(null);
                        }
                    }
                })
        );
    }

    private void updateTask(TaskDTO taskDTO) {
        Task task = taskDTO.getTask();
        task.update(taskDTO.getTitle(), taskDTO.getDescription());

        subscriptions.add(taskRepository.update(task)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        errorSaveTaskSubject.onNext(null);
                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            successSaveTaskSubject.onNext(null);
                        } else {
                            errorSaveTaskSubject.onNext(null);
                        }
                    }
                })
        );

    }

    public void changeCompleteState(Task task, boolean completed) {
        if (completed) {
            task.completeTask();
        } else {
            task.activateTask();
        }

        subscriptions.add(taskRepository.update(task)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        errorChangeCompleteStateSubject.onNext(null);

                        // rollback
                        if (completed) {
                            task.activateTask();
                        } else {
                            task.completeTask();
                        }
                    }

                    @Override
                    public void onNext(Boolean result) {
                    }
                })
        );
    }

    public void deleteTask(Task task) {

        taskRepository.delete(task).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Timber.w(e);
                errorDeleteTaskSubject.onNext(null);
            }

            @Override
            public void onNext(Boolean result) {
                if (result) {
                    successDeleteTaskSubject.onNext(null);
                } else {
                    errorDeleteTaskSubject.onNext(null);
                }
            }
        });
    }

    public void onDestroy() {
        subscriptions.clear();
    }

    // ----------------------
    // Notification
    // ----------------------

    // getTasks
    private final PublishSubject<List<Task>> tasksSubject = PublishSubject.create();
    public final Observable<List<Task>> tasks = tasksSubject.asObservable();
    private final PublishSubject<Void> errorGetTasksSubject = PublishSubject.create();
    public final Observable<Void> errorGetTasks = errorGetTasksSubject.asObservable();

    // save
    private final PublishSubject<Void> successSaveTaskSubject = PublishSubject.create();
    public final Observable<Void> successSaveTask = successSaveTaskSubject.asObservable();
    private final PublishSubject<Void> errorSaveTaskSubject = PublishSubject.create();
    public final Observable<Void> errorSaveTask = errorSaveTaskSubject.asObservable();

    // validation
    private final PublishSubject<Void> validationErrorSubject = PublishSubject.create();
    public final Observable<Void> validationError = validationErrorSubject.asObservable();

    // completeTask, activateTask
    private final PublishSubject<Void> errorChangeCompleteStateSubject = PublishSubject.create();
    public final Observable<Void> errorChangeCompleteState = errorChangeCompleteStateSubject.asObservable();

    // delete
    private final PublishSubject<Void> successDeleteTaskSubject = PublishSubject.create();
    public final Observable<Void> successDeleteTask = successDeleteTaskSubject.asObservable();
    private final PublishSubject<Void> errorDeleteTaskSubject = PublishSubject.create();
    public final Observable<Void> errorDeleteTask = errorDeleteTaskSubject.asObservable();
}
