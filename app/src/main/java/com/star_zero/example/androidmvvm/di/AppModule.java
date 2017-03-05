package com.star_zero.example.androidmvvm.di;

import com.star_zero.example.androidmvvm.domain.task.TaskRepository;
import com.star_zero.example.androidmvvm.infrastructure.task.RealmTaskRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    TaskRepository provideTaskRepository() {
        return new RealmTaskRepository();
    }
}
