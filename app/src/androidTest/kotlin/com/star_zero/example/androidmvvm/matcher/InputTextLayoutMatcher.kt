package com.star_zero.example.androidmvvm.matcher

import android.content.res.Resources
import android.support.design.widget.TextInputLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View

import org.hamcrest.Description
import org.hamcrest.Matcher

object InputTextLayoutMatcher {

    fun isErrorEnabled(enabled: Boolean): Matcher<View> {
        return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("isErrorEnabled: " + enabled)
            }

            public override fun matchesSafely(view: TextInputLayout): Boolean {
                return enabled == view.isErrorEnabled
            }
        }
    }

    fun withErrorText(resId: Int): Matcher<View> {
        return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("error resource id: " + resId)
            }

            public override fun matchesSafely(view: TextInputLayout): Boolean {
                val expect: String
                try {
                    expect = view.resources.getString(resId)
                } catch (e: Resources.NotFoundException) {
                    return false
                }

                val actual = view.error!!.toString()

                return expect == actual
            }
        }
    }
}
