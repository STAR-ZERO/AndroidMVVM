package com.star_zero.example.androidmvvm.di

import com.star_zero.example.androidmvvm.MockApp
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(
        MockAppModule::class,
        ActivityBuilderModule::class))
@Singleton
interface MockAppComponent {

    @Component.Builder
    interface Builder {
        fun build(): MockAppComponent
    }

    fun inject(app: MockApp)
}
