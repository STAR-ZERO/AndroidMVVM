package com.star_zero.example.androidmvvm.infrastructure.task;

import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskId;
import com.star_zero.example.androidmvvm.domain.task.TaskRepository;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Emitter;
import rx.Observable;

public class RealmTaskRepository implements TaskRepository {

    @Override
    public TaskId generateTaskId() {
        return new TaskId(UUID.randomUUID().toString());
    }

    @Override
    public Observable<Boolean> save(Task task) {
        return Observable.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.beginTransaction();

                TaskTable taskTable = realm.createObject(TaskTable.class);

                taskTable.setId(task.id.value);
                taskTable.setTitle(task.getTitle());
                taskTable.setDescription(task.getDescription());
                taskTable.setCompleted(task.isCompleted());
                taskTable.setCreatedAt(task.getCreatedAt().getMillis());

                realm.commitTransaction();

                emitter.onNext(true);
                emitter.onCompleted();

            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }, Emitter.BackpressureMode.NONE);
    }

    @Override
    public Observable<Boolean> update(Task task) {
        return Observable.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.beginTransaction();

                TaskTable taskTable = realm.where(TaskTable.class).equalTo("id", task.id.value).findFirst();

                taskTable.setTitle(task.getTitle());
                taskTable.setDescription(task.getDescription());
                taskTable.setCompleted(task.isCompleted());
                taskTable.setCreatedAt(task.getCreatedAt().getMillis());

                realm.commitTransaction();

                emitter.onNext(true);
                emitter.onCompleted();

            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }, Emitter.BackpressureMode.NONE);
    }

    @Override
    public Observable<Boolean> delete(Task task) {
        return Observable.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.beginTransaction();

                TaskTable taskTable = realm.where(TaskTable.class).equalTo("id", task.id.value).findFirst();

                taskTable.deleteFromRealm();

                realm.commitTransaction();

                emitter.onNext(true);
                emitter.onCompleted();

            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }, Emitter.BackpressureMode.NONE);
    }

    @Override
    public Observable<List<Task>> getTasks() {
        return Observable.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                List<Task> tasks = new ArrayList<>();

                RealmResults<TaskTable> taskTables = realm.where(TaskTable.class).findAll().sort("createdAt");

                for (TaskTable taskTable : taskTables) {
                    @SuppressWarnings("deprecation")
                    Task task = Task.createFromPersistent(
                            new TaskId(taskTable.getId()),
                            taskTable.getTitle(),
                            taskTable.getDescription(),
                            taskTable.isCompleted(),
                            new DateTime(taskTable.getCreatedAt()));
                    tasks.add(task);
                }

                emitter.onNext(tasks);
                emitter.onCompleted();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }, Emitter.BackpressureMode.NONE);
    }

}
