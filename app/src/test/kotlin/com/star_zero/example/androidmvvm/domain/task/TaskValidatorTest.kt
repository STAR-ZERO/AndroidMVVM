package com.star_zero.example.androidmvvm.domain.task

import android.os.Build
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasItems
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = intArrayOf(Build.VERSION_CODES.N))
class TaskValidatorTest {

    @Test
    @Throws(Exception::class)
    fun validate() {
        val validator = TaskValidator()

        assertTrue(validator.validate("title", "description"))
        assertThat(validator.errors.size, `is`(0))

        assertTrue(validator.validate("title", ""))
        assertThat(validator.errors.size, `is`(0))

        assertFalse(validator.validate("", "description"))
        assertThat(validator.errors.size, `is`(1))
        assertThat(validator.errors, hasItems(TaskValidator.ErrorType.TITLE_EMPTY))

        var longTitle = ""
        for (i in 0..32) {
            longTitle += "a"
        }
        assertFalse(validator.validate(longTitle, "description"))
        assertThat(validator.errors.size, `is`(1))
        assertThat(validator.errors, hasItems(TaskValidator.ErrorType.TITLE_TOO_LONG))

        var longDescription = ""
        for (i in 0..512) {
            longDescription += "a"
        }
        assertFalse(validator.validate("title", longDescription))
        assertThat(validator.errors.size, `is`(1))
        assertThat(validator.errors, hasItems(TaskValidator.ErrorType.DESCRIPTION_TOO_LONG))

        assertFalse(validator.validate("", longDescription))
        assertThat(validator.errors.size, `is`(2))
        assertThat(validator.errors, hasItems(TaskValidator.ErrorType.TITLE_EMPTY))
        assertThat(validator.errors, hasItems(TaskValidator.ErrorType.DESCRIPTION_TOO_LONG))
    }

}