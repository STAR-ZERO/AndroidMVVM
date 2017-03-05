package com.star_zero.example.androidmvvm;

import com.star_zero.example.androidmvvm.di.AppComponent;
import com.star_zero.example.androidmvvm.di.DaggerMockAppComponent;
import com.star_zero.example.androidmvvm.di.MockAppModule;

public class MockApp extends App {

    @Override
    public AppComponent getAppComponent() {
        return DaggerMockAppComponent.builder()
                .mockAppModule(new MockAppModule())
                .build();
    }
}
