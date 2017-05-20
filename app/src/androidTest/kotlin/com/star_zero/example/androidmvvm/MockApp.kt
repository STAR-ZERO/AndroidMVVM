package com.star_zero.example.androidmvvm

import com.star_zero.example.androidmvvm.di.AppComponent
import com.star_zero.example.androidmvvm.di.DaggerMockAppComponent
import com.star_zero.example.androidmvvm.di.MockAppModule

class MockApp : App() {

    override val appComponent: AppComponent
        get() = DaggerMockAppComponent.builder()
                .mockAppModule(MockAppModule())
                .build()
}
