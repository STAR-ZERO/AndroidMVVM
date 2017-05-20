package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockAppModule {

    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository {
        return MockTaskRepository()
    }
}
