package com.star_zero.example.androidmvvm;

import timber.log.Timber;

public class DebugApp extends App {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
