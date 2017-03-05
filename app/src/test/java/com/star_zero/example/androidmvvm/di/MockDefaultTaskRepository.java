package com.star_zero.example.androidmvvm.di;

import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskId;
import com.star_zero.example.androidmvvm.domain.task.TaskRepository;

import java.util.List;

import rx.Observable;

public abstract class MockDefaultTaskRepository implements TaskRepository {

    @Override
    public TaskId generateTaskId() {
        return null;
    }

    @Override
    public Observable<Boolean> save(Task task) {
        return null;
    }

    @Override
    public Observable<Boolean> update(Task task) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(Task task) {
        return null;
    }

    @Override
    public Observable<List<Task>> getTasks() {
        return null;
    }
}
