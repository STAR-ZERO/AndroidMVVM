package com.star_zero.example.androidmvvm.di

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.infrastructure.AppDatabase
import com.star_zero.example.androidmvvm.infrastructure.task.RoomTaskRepository
import com.star_zero.example.androidmvvm.presentation.shared.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = arrayOf(ViewModelSubComponent::class))
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "task.db").build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: AppDatabase): TaskRepository {
        return RoomTaskRepository(db)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(viewModelSubComponent: ViewModelSubComponent.Builder): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelSubComponent.build())
    }
}
