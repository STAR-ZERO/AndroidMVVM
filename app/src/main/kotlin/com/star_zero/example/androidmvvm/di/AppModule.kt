package com.star_zero.example.androidmvvm.di

import android.arch.lifecycle.ViewModelProvider
import com.star_zero.example.androidmvvm.domain.task.TaskRepository
import com.star_zero.example.androidmvvm.infrastructure.task.RealmTaskRepository
import com.star_zero.example.androidmvvm.presentation.shared.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = arrayOf(ViewModelSubComponent::class))
class AppModule {

    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository {
        return RealmTaskRepository()
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(viewModelSubComponent: ViewModelSubComponent.Builder): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelSubComponent.build())
    }
}
