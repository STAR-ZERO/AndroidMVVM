package com.star_zero.example.androidmvvm.di;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {MockAppModule.class})
@Singleton
public interface MockAppComponent extends AppComponent {
}
