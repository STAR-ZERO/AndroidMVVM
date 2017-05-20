package com.star_zero.example.androidmvvm.presentation.shared.view

import android.support.v7.app.AppCompatActivity
import com.star_zero.example.androidmvvm.App

abstract class BaseActivity : AppCompatActivity() {

    protected val appComponent by lazy {
        (application as App).appComponent
    }
}