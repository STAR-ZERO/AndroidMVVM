package com.star_zero.example.androidmvvm.di;

import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskId;
import com.star_zero.example.androidmvvm.domain.task.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MockTaskRepository implements TaskRepository {

    @Override
    public TaskId generateTaskId() {
        return new TaskId("id");
    }

    @Override
    public Observable<Boolean> save(Task task) {
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> update(Task task) {
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> delete(Task task) {
        return Observable.just(true);
    }

    @Override
    public Observable<List<Task>> getTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(Task.createNewTask(new TaskId("id1"), "title1", "description1"));
        tasks.add(Task.createNewTask(new TaskId("id2"), "title2", "description2"));
        return Observable.just(tasks);
    }
}
