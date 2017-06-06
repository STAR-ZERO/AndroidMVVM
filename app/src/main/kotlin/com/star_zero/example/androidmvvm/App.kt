package com.star_zero.example.androidmvvm

import android.app.Activity
import android.app.Application
import com.star_zero.example.androidmvvm.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import net.danlew.android.joda.JodaTimeAndroid
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

open class App: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        Realm.init(this)

        EventBus.builder().addIndex(EventBusIndex()).installDefaultEventBus()

        DaggerAppComponent.builder().build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}

