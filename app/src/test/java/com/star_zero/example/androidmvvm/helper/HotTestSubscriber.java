package com.star_zero.example.androidmvvm.helper;

import rx.observers.TestSubscriber;


public class HotTestSubscriber<T> extends TestSubscriber<T> {

    private int count;

    public HotTestSubscriber(int count) {
        this.count = count;
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);

        count--;

        // when call onNext specific number of times, subscriber terminate.
        if (count == 0) {
            onCompleted();
        }
    }

    public static <T> HotTestSubscriber<T> create() {
        return new HotTestSubscriber<>(1);
    }

    public static <T> HotTestSubscriber<T> create(int count) {
        return new HotTestSubscriber<>(count);
    }
}
