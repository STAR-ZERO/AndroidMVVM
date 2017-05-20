package com.star_zero.example.androidmvvm.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(MockAppModule::class))
@Singleton
interface MockAppComponent : AppComponent
