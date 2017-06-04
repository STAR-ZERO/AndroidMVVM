package com.star_zero.example.androidmvvm.infrastructure.task

import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import io.reactivex.Observable
import io.realm.Realm
import org.joda.time.DateTime
import java.util.UUID

class RealmTaskRepository : TaskRepository {

    override fun generateTaskId(): TaskId {
        return TaskId(UUID.randomUUID().toString())
    }

    override fun save(task: Task): Observable<Boolean> {
        return Observable.create({ emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.beginTransaction()

                    val taskTable = realm.createObject(TaskTable::class.java)
                    taskTable.id = task.id.value
                    taskTable.title = task.title
                    taskTable.description = task.description
                    taskTable.completed = task.completed
                    taskTable.createdAt = task.createdAt.millis

                    realm.commitTransaction()

                    emitter.onNext(true)
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

    override fun update(task: Task): Observable<Boolean> {
        return Observable.create({ emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.beginTransaction()

                    val taskTable = realm.where(TaskTable::class.java).equalTo("id", task.id.value).findFirst()

                    taskTable.title = task.title
                    taskTable.description = task.description
                    taskTable.completed = task.completed
                    taskTable.createdAt = task.createdAt.millis

                    realm.commitTransaction()

                    emitter.onNext(true)
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

    override fun delete(task: Task): Observable<Boolean> {
        return Observable.create({ emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.beginTransaction()

                    val taskTable = realm.where(TaskTable::class.java).equalTo("id", task.id.value).findFirst()

                    taskTable.deleteFromRealm()

                    realm.commitTransaction()

                    emitter.onNext(true)
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }

    override fun fetchTasks(): Observable<List<Task>> {
        return Observable.create({ emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.beginTransaction()

                    val taskTables = realm.where(TaskTable::class.java).findAll().sort("createdAt")
                    val tasks = mutableListOf<Task>()

                    taskTables.forEach {
                        @SuppressWarnings("deprecation")
                        val task = Task.createFromPersistent(
                                TaskId(it.id!!),
                                it.title!!,
                                it.description,
                                it.completed,
                                DateTime(it.createdAt)
                        )
                        tasks.add(task)
                    }

                    emitter.onNext(tasks)
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
    }
}