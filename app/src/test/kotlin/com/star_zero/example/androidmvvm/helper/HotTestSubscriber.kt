package com.star_zero.example.androidmvvm.helper

import rx.observers.TestSubscriber


class HotTestSubscriber<T>(private var count: Int) : TestSubscriber<T>() {

    override fun onNext(t: T) {
        super.onNext(t)

        count--

        // when call onNext specific number of times, subscriber terminate.
        if (count == 0) {
            onCompleted()
        }
    }

    companion object {

        fun <T> create(): HotTestSubscriber<T> {
            return HotTestSubscriber(1)
        }

        fun <T> create(count: Int): HotTestSubscriber<T> {
            return HotTestSubscriber(count)
        }
    }
}
