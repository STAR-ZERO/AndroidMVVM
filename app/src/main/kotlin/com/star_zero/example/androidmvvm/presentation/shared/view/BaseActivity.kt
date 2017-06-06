package com.star_zero.example.androidmvvm.presentation.shared.view

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), LifecycleRegistryOwner {
    val registry: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return registry
    }

}

