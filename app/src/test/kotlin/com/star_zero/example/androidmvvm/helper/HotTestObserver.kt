package com.star_zero.example.androidmvvm.helper

import io.reactivex.observers.TestObserver

class HotTestObserver<T>(private var count: Int) : TestObserver<T>() {

    override fun onNext(t: T) {
        super.onNext(t)

        count--

        // when call onNext specific number of times, subscriber terminate.
        if (count == 0) {
            onComplete()
        }
    }

    companion object {

        fun <T> create(): HotTestObserver<T> {
            return HotTestObserver(1)
        }

        fun <T> create(count: Int): HotTestObserver<T> {
            return HotTestObserver(count)
        }
    }
}
