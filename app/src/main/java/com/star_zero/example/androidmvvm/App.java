package com.star_zero.example.androidmvvm;

import android.app.Application;

import com.star_zero.example.androidmvvm.di.AppComponent;
import com.star_zero.example.androidmvvm.di.AppModule;
import com.star_zero.example.androidmvvm.di.DaggerAppComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        Realm.init(this);

        EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
