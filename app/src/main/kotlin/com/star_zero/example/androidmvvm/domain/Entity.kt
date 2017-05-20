package com.star_zero.example.androidmvvm.domain

import android.databinding.BaseObservable

abstract class Entity<out T : Identifier>(val id: T) : BaseObservable() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Entity<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
