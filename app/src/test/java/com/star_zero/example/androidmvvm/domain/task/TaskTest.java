package com.star_zero.example.androidmvvm.domain.task;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.N)
public class TaskTest {

    @Test
    public void completeTask() throws Exception {
        Task task = Task.createNewTask(new TaskId("1"), "title", "description");
        task.completeTask();
        assertTrue(task.isCompleted());
    }

    @Test
    public void activateTask() throws Exception {
        Task task = Task.createNewTask(new TaskId("1"), "title", "description");
        task.completeTask();
        task.activateTask();
        assertFalse(task.isCompleted());
    }

    @Test
    public void update() throws Exception {
        Task task = Task.createNewTask(new TaskId("1"), "title", "description");
        task.update("title update", "description update");
        assertThat(task.getTitle(), is("title update"));
        assertThat(task.getDescription(), is("description update"));
    }

}