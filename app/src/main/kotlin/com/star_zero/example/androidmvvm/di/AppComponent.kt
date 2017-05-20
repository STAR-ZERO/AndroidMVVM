package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity
import com.star_zero.example.androidmvvm.presentation.tasks.TasksActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    fun inject(activity: TasksActivity)

    fun inject(activity: AddEditTaskActivity)
}