package com.star_zero.example.androidmvvm.presentation.shared.view;

import android.support.v7.app.AppCompatActivity;

import com.star_zero.example.androidmvvm.App;
import com.star_zero.example.androidmvvm.di.AppComponent;

public abstract class BaseActivity extends AppCompatActivity {

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }
}
