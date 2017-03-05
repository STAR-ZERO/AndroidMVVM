package com.star_zero.example.androidmvvm.presentation.add_edit_task;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskId;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.star_zero.example.androidmvvm.matcher.InputTextLayoutMatcher.isErrorEnabled;
import static com.star_zero.example.androidmvvm.matcher.InputTextLayoutMatcher.withErrorText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEditTaskActivityTest {

    @Rule
    public ActivityTestRule<AddEditTaskActivity> activityRule = new ActivityTestRule<>(AddEditTaskActivity.class);

    @Test
    public void initViewNewTask() {
        onView(withId(R.id.edit_title)).check(matches(withText("")));
        onView(withId(R.id.edit_description)).check(matches(withText("")));
        onView(withId(R.id.menu_delete)).check(doesNotExist());
    }

    @Test
    public void initViewUpdateTask() {
        Intent intent = new Intent();
        intent.putExtra("task", Task.createNewTask(new TaskId("id"), "title", "description"));
        activityRule.launchActivity(intent);

        onView(withId(R.id.edit_title)).check(matches(withText("title")));
        onView(withId(R.id.edit_description)).check(matches(withText("description")));
        onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));
    }

    @Test
    public void saveSuccess() {
        onView(withId(R.id.edit_title)).perform(typeText("title"));
        onView(withId(R.id.edit_description)).perform(typeText("description"));

        onView(withId(R.id.button_save)).perform(click());

        assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void saveValidationError() {
        onView(withId(R.id.edit_title)).perform(typeText(""));
        onView(withId(R.id.edit_description)).perform(typeText(""));

        onView(withId(R.id.button_save)).perform(click());

        onView(withId(R.id.input_layout_title)).check(matches(isErrorEnabled(true)));
        onView(withId(R.id.input_layout_title)).check(matches(withErrorText(R.string.validation_error_title_empty)));
    }

    @Test
    public void deleteSuccess() {
        Intent intent = new Intent();
        intent.putExtra("task", Task.createNewTask(new TaskId("id"), "title", "description"));
        activityRule.launchActivity(intent);

        onView(withId(R.id.menu_delete)).perform(click());

        assertTrue(activityRule.getActivity().isFinishing());
    }
}