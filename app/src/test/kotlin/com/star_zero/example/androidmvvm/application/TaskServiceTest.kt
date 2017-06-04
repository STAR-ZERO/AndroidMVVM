package com.star_zero.example.androidmvvm.application

import android.os.Build
import com.star_zero.example.androidmvvm.application.dto.TaskDTO
import com.star_zero.example.androidmvvm.di.MockDefaultTaskRepository
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.helper.HotTestObserver
import com.star_zero.example.androidmvvm.utils.Irrelevant
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.hamcrest.Matchers.not
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = intArrayOf(Build.VERSION_CODES.N))
class TaskServiceTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({
            Schedulers.trampoline()
        })
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        RxAndroidPlugins.reset()
    }

    @Test
    @Throws(Exception::class)
    fun fetchTasks() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun fetchTasks(): Observable<List<Task>> {
                val tasks = ArrayList<Task>()
                tasks.add(Task.createNewTask(TaskId("id"), "title", "description"))
                return Observable.just<List<Task>>(tasks)
            }
        })

        val testObserver = HotTestObserver.create<List<Task>>()
        service.tasks.subscribe(testObserver)

        service.fetchTasks()

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

        val tasks = testObserver.values()[0]
        assertThat(tasks[0].id, `is`(TaskId("id")))
    }

    @Test
    @Throws(Exception::class)
    fun fetchTasksError() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun fetchTasks(): Observable<List<Task>> {
                return Observable.error<List<Task>>(Exception())
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.errorFetchTasks.subscribe(testObserver)

        service.fetchTasks()

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

    @Test
    @Throws(Exception::class)
    fun saveValidationError() {
        val service = TaskService(object : MockDefaultTaskRepository() {
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.validationError.subscribe(testObserver)

        val dto = TaskDTO()
        service.save(dto)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

        assertThat(dto.titleError, not(0))
    }

    @Test
    @Throws(Exception::class)
    fun saveNew() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun generateTaskId(): TaskId {
                return TaskId("id")
            }

            override fun save(task: Task): Observable<Boolean> {
                return Observable.just(true)
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.successSaveTask.subscribe(testObserver)

        val dto = TaskDTO()
        dto.title = "title"
        dto.description = "description"
        service.save(dto)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

    @Test
    @Throws(Exception::class)
    fun saveNewError() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun generateTaskId(): TaskId {
                return TaskId("id")
            }

            override fun save(task: Task): Observable<Boolean> {
                return Observable.error<Boolean>(Exception())
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.errorSaveTask.subscribe(testObserver)

        val dto = TaskDTO()
        dto.title = "title"
        dto.description = "description"
        service.save(dto)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

    @Test
    @Throws(Exception::class)
    fun saveUpdate() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun update(task: Task): Observable<Boolean> {
                return Observable.just(true)
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.successSaveTask.subscribe(testObserver)

        val task = Task.createNewTask(TaskId("id"), "title", "description")
        val dto = TaskDTO.createFromTask(task)
        service.save(dto)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

    @Test
    @Throws(Exception::class)
    fun saveUpdateError() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun update(task: Task): Observable<Boolean> {
                return Observable.error<Boolean>(Exception())
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.errorSaveTask.subscribe(testObserver)

        val task = Task.createNewTask(TaskId("id"), "title", "description")
        val dto = TaskDTO.createFromTask(task)
        service.save(dto)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

    @Test
    @Throws(Exception::class)
    fun changeCompleteState() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun update(task: Task): Observable<Boolean> {
                return Observable.just(true)
            }
        })

        val task = Task.createNewTask(TaskId("id"), "title", "description")

        service.changeCompleteState(task, true)

        assertTrue(task.completed)
    }

    @Test
    @Throws(Exception::class)
    fun changeCompleteStateError() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun update(task: Task): Observable<Boolean> {
                return Observable.error<Boolean>(Exception())
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.errorChangeCompleteState.subscribe(testObserver)

        val task = Task.createNewTask(TaskId("id"), "title", "description")

        service.changeCompleteState(task, true)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

        assertFalse(task.completed)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun delete(task: Task): Observable<Boolean> {
                return Observable.just(true)
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.successDeleteTask.subscribe(testObserver)

        val task = Task.createNewTask(TaskId("id"), "title", "description")
        service.deleteTask(task)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTaskError() {
        val service = TaskService(object : MockDefaultTaskRepository() {
            override fun delete(task: Task): Observable<Boolean> {
                return Observable.error<Boolean>(Exception())
            }
        })

        val testObserver = HotTestObserver.create<Irrelevant>()
        service.errorDeleteTask.subscribe(testObserver)

        val task = Task.createNewTask(TaskId("id"), "title", "description")
        service.deleteTask(task)

        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
    }

}