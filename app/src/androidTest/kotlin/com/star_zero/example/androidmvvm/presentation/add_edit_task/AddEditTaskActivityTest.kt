package com.star_zero.example.androidmvvm.presentation.add_edit_task

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskId
import com.star_zero.example.androidmvvm.matcher.InputTextLayoutMatcher.isErrorEnabled
import com.star_zero.example.androidmvvm.matcher.InputTextLayoutMatcher.withErrorText
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddEditTaskActivityTest {

    @Rule @JvmField
    var activityRule = ActivityTestRule(AddEditTaskActivity::class.java)

    @Test
    fun initViewNewTask() {
        onView(withId(R.id.edit_title)).check(matches(withText("")))
        onView(withId(R.id.edit_description)).check(matches(withText("")))
        onView(withId(R.id.menu_delete)).check(doesNotExist())
    }

    @Test
    fun initViewUpdateTask() {
        val intent = Intent()
        intent.putExtra("task", Task.createNewTask(TaskId("id"), "title", "description"))
        activityRule.launchActivity(intent)

        onView(withId(R.id.edit_title)).check(matches(withText("title")))
        onView(withId(R.id.edit_description)).check(matches(withText("description")))
        onView(withId(R.id.menu_delete)).check(matches(isDisplayed()))
    }

    @Test
    fun saveSuccess() {
        onView(withId(R.id.edit_title)).perform(typeText("title"))
        onView(withId(R.id.edit_description)).perform(typeText("description"))

        onView(withId(R.id.button_save)).perform(click())

        assertTrue(activityRule.activity.isFinishing)
    }

    @Test
    fun saveValidationError() {
        onView(withId(R.id.edit_title)).perform(typeText(""))
        onView(withId(R.id.edit_description)).perform(typeText(""))

        onView(withId(R.id.button_save)).perform(click())

        onView(withId(R.id.input_layout_title)).check(matches(isErrorEnabled(true)))
        onView(withId(R.id.input_layout_title)).check(matches(withErrorText(R.string.validation_error_title_empty)))
    }

    @Test
    fun deleteSuccess() {
        val intent = Intent()
        intent.putExtra("task", Task.createNewTask(TaskId("id"), "title", "description"))
        activityRule.launchActivity(intent)

        onView(withId(R.id.menu_delete)).perform(click())

        assertTrue(activityRule.activity.isFinishing)
    }
}