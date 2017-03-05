package com.star_zero.example.androidmvvm.di;

import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity;
import com.star_zero.example.androidmvvm.presentation.tasks.TasksActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(TasksActivity activity);

    void inject(AddEditTaskActivity activity);
}
