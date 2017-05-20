package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.infrastructure.task.RealmTaskRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository {
        return RealmTaskRepository()
    }
}