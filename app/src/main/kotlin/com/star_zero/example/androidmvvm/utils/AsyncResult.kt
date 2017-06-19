package com.star_zero.example.androidmvvm.utils

sealed class AsyncResult<out T> {
    data class Success<out T>(val value: T): AsyncResult<T>()
    data class Error<out T>(val error: Exception): AsyncResult<T>()
}
