package com.star_zero.example.androidmvvm.utils.extension

import android.arch.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.fire() {
    value = null
}

