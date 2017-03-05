package com.star_zero.example.androidmvvm.domain.task;

import java.util.List;

import rx.Observable;

public interface TaskRepository {

    TaskId generateTaskId();

    Observable<Boolean> save(Task task);

    Observable<Boolean> update(Task task);

    Observable<Boolean> delete(Task task);

    Observable<List<Task>> getTasks();

}
