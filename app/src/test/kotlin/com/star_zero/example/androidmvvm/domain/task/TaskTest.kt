package com.star_zero.example.androidmvvm.domain.task

import android.os.Build
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = intArrayOf(Build.VERSION_CODES.N))
class TaskTest {

    @Test
    @Throws(Exception::class)
    fun completeTask() {
        val task = Task.createNewTask(TaskId("1"), "title", "description")
        task.completeTask()
        assertTrue(task.completed)
    }

    @Test
    @Throws(Exception::class)
    fun activateTask() {
        val task = Task.createNewTask(TaskId("1"), "title", "description")
        task.completeTask()
        task.activateTask()
        assertFalse(task.completed)
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        val task = Task.createNewTask(TaskId("1"), "title", "description")
        task.update("title update", "description update")
        assertThat<String>(task.title, `is`("title update"))
        assertThat<String>(task.description, `is`("description update"))
    }

}