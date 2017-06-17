package com.star_zero.example.androidmvvm.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

class PublishLiveData<T> : MutableLiveData<T>() {

    private var internalLiveData = MutableLiveData<T>()

    override fun observe(owner: LifecycleOwner?, observer: Observer<T>?) {
        internalLiveData = MutableLiveData<T>()
        internalLiveData.observe(owner, observer)
    }

    override fun setValue(value: T?) {
        super.setValue(value)   // It need to increment internal version to use Transformations.map or Transformations.switchMap.
        internalLiveData.value = value
    }

    override fun getValue(): T? {
        return internalLiveData.value
    }

    override fun postValue(value: T?) {
        internalLiveData.postValue(value)
    }
}
