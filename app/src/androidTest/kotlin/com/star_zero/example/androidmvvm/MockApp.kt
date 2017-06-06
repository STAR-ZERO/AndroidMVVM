package com.star_zero.example.androidmvvm

import com.star_zero.example.androidmvvm.di.DaggerMockAppComponent

class MockApp : App() {

    override fun onCreate() {
        super.onCreate()

        DaggerMockAppComponent.builder().build().inject(this)
    }
}
