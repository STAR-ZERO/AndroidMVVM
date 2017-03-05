package com.star_zero.example.androidmvvm.domain;

import android.support.annotation.NonNull;

public abstract class Identifier {

    @NonNull
    public final String value;

    public Identifier(@NonNull  String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
