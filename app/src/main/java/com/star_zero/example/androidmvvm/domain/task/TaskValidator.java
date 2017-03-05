package com.star_zero.example.androidmvvm.domain.task;

import java.util.ArrayList;
import java.util.List;

public class TaskValidator {

    public enum ErrorType {
        TITLE_EMPTY,
        TITLE_TOO_LONG,
        DESCRIPTION_TOO_LONG
    }

    private List<ErrorType> errors;

    public boolean validate(String title, String description) {
        errors = new ArrayList<>();

        // title
        if (title == null || title.length() == 0) {
            errors.add(ErrorType.TITLE_EMPTY);
        } else if (title.length() > 32) {
            errors.add(ErrorType.TITLE_TOO_LONG);
        }

        // description
        if (description != null && description.length() > 512) {
            errors.add(ErrorType.DESCRIPTION_TOO_LONG);
        }

        return errors.size() == 0;
    }

    public List<ErrorType> getErrors() {
        return errors;
    }
}
