package com.star_zero.example.androidmvvm.matcher;

import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class InputTextLayoutMatcher {

    public static Matcher<View> isErrorEnabled(final boolean enabled) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("isErrorEnabled: " + enabled);
            }

            @Override
            public boolean matchesSafely(TextInputLayout view) {
                return enabled == view.isErrorEnabled();
            }
        };
    }

    public static Matcher<View> withErrorText(final int resId) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("error resource id: " + resId);
            }

            @Override
            public boolean matchesSafely(TextInputLayout view) {
                String expect;
                try {
                    expect = view.getResources().getString(resId);
                } catch (Resources.NotFoundException e) {
                    return false;
                }

                String actual = view.getError().toString();

                return expect.equals(actual);
            }
        };
    }
}
