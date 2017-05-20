package com.star_zero.example.androidmvvm.domain.task

class TaskValidator {

    enum class ErrorType {
        TITLE_EMPTY,
        TITLE_TOO_LONG,
        DESCRIPTION_TOO_LONG
    }

    private val _errors = mutableListOf<ErrorType>()
    val errors: List<ErrorType>
        get() = _errors.toList()

    fun validate(title: String?, description: String?): Boolean {
        _errors.clear()

        // title
        if (title == null || title.isEmpty()) {
            _errors.add(ErrorType.TITLE_EMPTY)
        } else if (title.length > 32) {
            _errors.add(ErrorType.TITLE_TOO_LONG)
        }

        // description
        if (description != null && description.length > 512) {
            _errors.add(ErrorType.DESCRIPTION_TOO_LONG)
        }

        return _errors.size == 0
    }

}