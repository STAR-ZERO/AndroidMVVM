package com.star_zero.example.androidmvvm.presentation.tasks

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasMyPackageName
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksActivityTest {

    @Rule @JvmField
    var intentsRule = IntentsTestRule(TasksActivity::class.java)

    @Test
    fun initView() {
        onView(withId(R.id.recycler_tasks)).check(matches(hasDescendant(withText("title1"))))
        onView(withId(R.id.recycler_tasks)).check(matches(hasDescendant(withText("title2"))))
    }

    @Test
    fun clickNewTask() {
        onView(withId(R.id.button_new_task)).perform(click())

        intended(allOf<Intent>(
                hasComponent(hasMyPackageName()),
                hasComponent(hasClassName(AddEditTaskActivity::class.java.name))
        ))
    }

    @Test
    fun clickItem() {
        onView(withId(R.id.recycler_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        intended(allOf<Intent>(
                hasComponent(hasMyPackageName()),
                hasComponent(hasClassName(AddEditTaskActivity::class.java.name)),
                hasExtra(`is`("task"), instanceOf<Any>(Task::class.java))
        ))
    }
}