package com.star_zero.example.androidmvvm.application;

import android.os.Build;

import com.star_zero.example.androidmvvm.application.dto.TaskDTO;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskId;
import com.star_zero.example.androidmvvm.helper.HotTestSubscriber;
import com.star_zero.example.androidmvvm.di.MockDefaultTaskRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.N)
public class TaskServiceTest {

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void getTasks() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<List<Task>> getTasks() {
                List<Task> tasks = new ArrayList<>();
                tasks.add(Task.createNewTask(new TaskId("id"), "title", "description"));
                return Observable.just(tasks);
            }
        });

        HotTestSubscriber<List<Task>> testSubscriber = HotTestSubscriber.create();
        service.tasks.subscribe(testSubscriber);

        service.getTasks();

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);

        List<Task> tasks = testSubscriber.getOnNextEvents().get(0);
        assertThat(tasks.get(0).id, is(new TaskId("id")));
    }

    @Test
    public void getTasksError() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<List<Task>> getTasks() {
                return Observable.error(new Exception());
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.errorGetTasks.subscribe(testSubscriber);

        service.getTasks();

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void saveValidationError() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.validationError.subscribe(testSubscriber);

        TaskDTO dto = new TaskDTO();
        service.save(dto);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);

        assertThat(dto.getTitleError(), not(0));
    }

    @Test
    public void saveNew() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public TaskId generateTaskId() {
                return new TaskId("id");
            }

            @Override
            public Observable<Boolean> save(Task task) {
                return Observable.just(true);
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.successSaveTask.subscribe(testSubscriber);

        TaskDTO dto = new TaskDTO();
        dto.setTitle("title");
        dto.setDescription("description");
        service.save(dto);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void saveNewError() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public TaskId generateTaskId() {
                return new TaskId("id");
            }

            @Override
            public Observable<Boolean> save(Task task) {
                return Observable.error(new Exception());
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.errorSaveTask.subscribe(testSubscriber);

        TaskDTO dto = new TaskDTO();
        dto.setTitle("title");
        dto.setDescription("description");
        service.save(dto);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void saveUpdate() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<Boolean> update(Task task) {
                return Observable.just(true);
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.successSaveTask.subscribe(testSubscriber);

        Task task = Task.createNewTask(new TaskId("id"), "title", "description");
        TaskDTO dto = TaskDTO.createFromTask(task);
        service.save(dto);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void saveUpdateError() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<Boolean> update(Task task) {
                return Observable.error(new Exception());
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.errorSaveTask.subscribe(testSubscriber);

        Task task = Task.createNewTask(new TaskId("id"), "title", "description");
        TaskDTO dto = TaskDTO.createFromTask(task);
        service.save(dto);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void changeCompleteState() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<Boolean> update(Task task) {
                return Observable.just(true);
            }
        });

        Task task = Task.createNewTask(new TaskId("id"), "title", "description");

        service.changeCompleteState(task, true);

        assertTrue(task.isCompleted());
    }

    @Test
    public void changeCompleteStateError() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<Boolean> update(Task task) {
                return Observable.error(new Exception());
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.errorChangeCompleteState.subscribe(testSubscriber);

        Task task = Task.createNewTask(new TaskId("id"), "title", "description");

        service.changeCompleteState(task, true);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);

        assertFalse(task.isCompleted());
    }

    @Test
    public void deleteTask() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<Boolean> delete(Task task) {
                return Observable.just(true);
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.successDeleteTask.subscribe(testSubscriber);

        Task task = Task.createNewTask(new TaskId("id"), "title", "description");
        service.deleteTask(task);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void deleteTaskError() throws Exception {
        TaskService service = new TaskService(new MockDefaultTaskRepository() {
            @Override
            public Observable<Boolean> delete(Task task) {
                return Observable.error(new Exception());
            }
        });

        HotTestSubscriber<Void> testSubscriber = HotTestSubscriber.create();
        service.errorDeleteTask.subscribe(testSubscriber);

        Task task = Task.createNewTask(new TaskId("id"), "title", "description");
        service.deleteTask(task);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

}