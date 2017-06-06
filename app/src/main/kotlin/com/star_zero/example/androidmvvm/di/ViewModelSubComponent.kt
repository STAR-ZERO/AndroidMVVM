package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskViewModel
import com.star_zero.example.androidmvvm.presentation.tasks.TasksViewModel
import dagger.Subcomponent

@Subcomponent
interface ViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun tasksViewModel(): TasksViewModel
    fun addEditTaskViewModel(): AddEditTaskViewModel
}