package com.star_zero.example.androidmvvm.domain.task;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.N)
public class TaskValidatorTest {

    @Test
    public void validate() throws Exception {
        TaskValidator validator = new TaskValidator();

        assertTrue(validator.validate("title", "description"));
        assertThat(validator.getErrors().size(), is(0));

        assertTrue(validator.validate("title", ""));
        assertThat(validator.getErrors().size(), is(0));

        assertFalse(validator.validate("", "description"));
        assertThat(validator.getErrors().size(), is(1));
        assertThat(validator.getErrors(), hasItems(TaskValidator.ErrorType.TITLE_EMPTY));

        String longTitle = "";
        for (int i = 0; i < 33; i++) {
            longTitle += "a";
        }
        assertFalse(validator.validate(longTitle, "description"));
        assertThat(validator.getErrors().size(), is(1));
        assertThat(validator.getErrors(), hasItems(TaskValidator.ErrorType.TITLE_TOO_LONG));

        String longDescription = "";
        for (int i = 0; i < 513; i++) {
            longDescription += "a";
        }
        assertFalse(validator.validate("title", longDescription));
        assertThat(validator.getErrors().size(), is(1));
        assertThat(validator.getErrors(), hasItems(TaskValidator.ErrorType.DESCRIPTION_TOO_LONG));

        assertFalse(validator.validate("", longDescription));
        assertThat(validator.getErrors().size(), is(2));
        assertThat(validator.getErrors(), hasItems(TaskValidator.ErrorType.TITLE_EMPTY));
        assertThat(validator.getErrors(), hasItems(TaskValidator.ErrorType.DESCRIPTION_TOO_LONG));
    }

}