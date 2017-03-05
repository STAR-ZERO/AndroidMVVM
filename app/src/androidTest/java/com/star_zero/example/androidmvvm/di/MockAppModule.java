package com.star_zero.example.androidmvvm.di;

import com.star_zero.example.androidmvvm.domain.task.TaskRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockAppModule {

    @Provides
    @Singleton
    TaskRepository provideTaskRepository() {
        return new MockTaskRepository();
    }
}
