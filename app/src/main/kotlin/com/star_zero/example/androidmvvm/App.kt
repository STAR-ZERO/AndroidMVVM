package com.star_zero.example.androidmvvm

import android.app.Application
import com.star_zero.example.androidmvvm.di.AppComponent
import com.star_zero.example.androidmvvm.di.AppModule
import com.star_zero.example.androidmvvm.di.DaggerAppComponent
import io.realm.Realm
import net.danlew.android.joda.JodaTimeAndroid
import org.greenrobot.eventbus.EventBus

open class App: Application() {

    open val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule())
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        Realm.init(this)

        EventBus.builder().addIndex(EventBusIndex()).installDefaultEventBus()
    }
}

