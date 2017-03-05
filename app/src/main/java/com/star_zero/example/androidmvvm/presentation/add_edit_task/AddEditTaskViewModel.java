package com.star_zero.example.androidmvvm.presentation.add_edit_task;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.application.TaskService;
import com.star_zero.example.androidmvvm.application.dto.TaskDTO;
import com.star_zero.example.androidmvvm.domain.task.Task;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class AddEditTaskViewModel {

    private static final String STATE_KEY_TASK = "task";

    public final ObservableField<TaskDTO> taskDTO = new ObservableField<>();

    private TaskService taskService;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    AddEditTaskViewModel(TaskService taskService) {
        this.taskService = taskService;
        this.taskDTO.set(new TaskDTO());
    }

    void onCreate() {
        subscribe();
    }

    void onDestroy() {
        taskService.onDestroy();
        subscriptions.clear();
    }

    void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;
        taskDTO.set(savedInstanceState.getParcelable(STATE_KEY_TASK));
    }

    void saveState(Bundle outState) {
        outState.putParcelable(STATE_KEY_TASK, taskDTO.get());
    }

    // ----------------------
    // Property
    // ----------------------

    void setTask(Task task) {
        this.taskDTO.set(TaskDTO.createFromTask(task));
    }

    boolean isSaved() {
        return taskDTO.get().getTask() != null;
    }

    // ----------------------
    // Event
    // ----------------------

    public void onClickSave(View view) {
        saveTask();
    }

    // ----------------------
    // operation
    // ----------------------

    private void saveTask() {
        taskService.save(taskDTO.get());
    }

    void deleteTask() {
        taskService.deleteTask(taskDTO.get().getTask());
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private void subscribe() {
        // save
        subscriptions.add(taskService.validationError.subscribe(aVoid -> {
            errorMessageSubject.onNext(R.string.error_validation);
        }));
        subscriptions.add(taskService.successSaveTask.subscribe(successSaveTaskSubject::onNext));
        subscriptions.add(taskService.errorSaveTask.subscribe(aVoid -> {
            errorMessageSubject.onNext(R.string.error_save_task);
        }));

        // delete
        subscriptions.add(taskService.successDeleteTask.subscribe(successDeleteTaskSubject::onNext));
        subscriptions.add(taskService.errorDeleteTask.subscribe(aVoid -> {
            errorMessageSubject.onNext(R.string.error_delete_task);
        }));
    }

    // ----------------------
    // Notification
    // ----------------------

    private final PublishSubject<Integer> errorMessageSubject = PublishSubject.create();
    final Observable<Integer> errorMessage = errorMessageSubject.asObservable();

    private final PublishSubject<Void> successSaveTaskSubject = PublishSubject.create();
    final Observable<Void> successSaveTask = successSaveTaskSubject.asObservable();

    private final PublishSubject<Void> successDeleteTaskSubject = PublishSubject.create();
    final Observable<Void> successDeleteTask = successDeleteTaskSubject.asObservable();
}
