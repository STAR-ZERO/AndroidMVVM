package com.star_zero.example.androidmvvm.presentation.shared.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.star_zero.example.androidmvvm.di.ViewModelSubComponent
import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskViewModel
import com.star_zero.example.androidmvvm.presentation.tasks.TasksViewModel
import java.util.concurrent.Callable
import javax.inject.Inject

class ViewModelFactory @Inject constructor(viewModelSubComponent: ViewModelSubComponent) : ViewModelProvider.Factory {

    val creators: Map<Class<*>, Callable<out ViewModel>> = mapOf(
            TasksViewModel::class.java to Callable(viewModelSubComponent::tasksViewModel),
            AddEditTaskViewModel::class.java to Callable(viewModelSubComponent::addEditTaskViewModel)
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
        creator ?: throw IllegalArgumentException("unknown model class $modelClass")

        @Suppress("UNCHECKED_CAST")
        return creator.call() as T
    }
}