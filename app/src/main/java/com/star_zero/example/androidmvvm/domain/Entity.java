package com.star_zero.example.androidmvvm.domain;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

public abstract class Entity<T extends Identifier> extends BaseObservable {

    @NonNull
    public final T id;

    protected Entity(@NonNull T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity<?> entity = (Entity<?>) o;

        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
