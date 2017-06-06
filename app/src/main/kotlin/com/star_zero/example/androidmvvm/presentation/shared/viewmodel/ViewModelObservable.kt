package com.star_zero.example.androidmvvm.presentation.shared.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.Observable.OnPropertyChangedCallback
import android.databinding.PropertyChangeRegistry

// ViewModel + BaseObservable
abstract class ViewModelObservable : ViewModel(), Observable {
    val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

}
