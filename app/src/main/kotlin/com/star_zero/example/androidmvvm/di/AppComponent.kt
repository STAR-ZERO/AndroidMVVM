package com.star_zero.example.androidmvvm.di

import android.app.Application
import com.star_zero.example.androidmvvm.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton



@Component(modules = arrayOf(
        AppModule::class,
        ActivityBuilderModule::class))
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}