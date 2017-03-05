package com.star_zero.example.androidmvvm.presentation.tasks;

import android.view.View;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.application.TaskService;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.presentation.tasks.adapter.TasksAdapter;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class TasksViewModel {

    private TaskService taskService;

    public final TasksAdapter adapter = new TasksAdapter();

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    TasksViewModel(TaskService taskService) {
        this.taskService = taskService;
    }

    void onCreate() {
        subscribe();
    }

    void onStart() {
        getTasks();
    }

    void onDestroy() {
        taskService.onDestroy();
        subscriptions.clear();
    }

    // ----------------------
    // Event
    // ----------------------

    public void onClickNewTask(View view) {
        clickNewTaskSubject.onNext(null);
    }

    // ----------------------
    // Operation
    // ----------------------

    private void getTasks() {
        taskService.getTasks();
    }

    void changeCompleteState(Task task, boolean completed) {
        taskService.changeCompleteState(task, completed);
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private void subscribe() {
        subscriptions.add(taskService.tasks.subscribe(adapter::setTasks));
        subscriptions.add(taskService.errorGetTasks.subscribe(aVoid -> {
            errorMessageSubject.onNext(R.string.error_get_task);
        }));
        subscriptions.add(taskService.errorChangeCompleteState.subscribe(aVoid -> {
            errorMessageSubject.onNext(R.string.error_change_state);
        }));
    }

    // ----------------------
    // Notification
    // ----------------------

    private final PublishSubject<Void> clickNewTaskSubject = PublishSubject.create();
    final Observable<Void> clickNewTask = clickNewTaskSubject.asObservable();

    private final PublishSubject<Integer> errorMessageSubject = PublishSubject.create();
    final Observable<Integer> errorMessage = errorMessageSubject.asObservable();
}
