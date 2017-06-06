package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity
import com.star_zero.example.androidmvvm.presentation.tasks.TasksActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeTasksActivity(): TasksActivity

    @ContributesAndroidInjector
    abstract fun contributeAddEditTaskActivity(): AddEditTaskActivity
}