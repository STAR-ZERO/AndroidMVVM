package com.star_zero.example.androidmvvm.presentation.tasks;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasMyPackageName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TasksActivityTest {

    @Rule
    public IntentsTestRule<TasksActivity> intentsRule = new IntentsTestRule<>(TasksActivity.class);

    @Test
    public void initView() {
        onView(withId(R.id.recycler_tasks)).check(matches(hasDescendant(withText("title1"))));
        onView(withId(R.id.recycler_tasks)).check(matches(hasDescendant(withText("title2"))));
    }

    @Test
    public void clickNewTask() {
        onView(withId(R.id.button_new_task)).perform(click());

        intended(allOf(
                hasComponent(hasMyPackageName()),
                hasComponent(hasClassName(AddEditTaskActivity.class.getName()))
        ));
    }

    @Test
    public void clickItem() {
        onView(withId(R.id.recycler_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(allOf(
                hasComponent(hasMyPackageName()),
                hasComponent(hasClassName(AddEditTaskActivity.class.getName())),
                hasExtra(is("task"), instanceOf(Task.class))
        ));
    }
}