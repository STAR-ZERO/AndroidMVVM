package com.star_zero.example.androidmvvm.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class AsyncLiveData<T> private constructor(private val exec: suspend () -> T) : LiveData<AsyncResult<T>>() {

    private var observer: Observer<AsyncResult<T>>? = null

    private var job: Job? = null

    companion object {
        fun <T> create(exec: suspend () -> T): AsyncLiveData<T> {
            return AsyncLiveData(exec)
        }
    }

    override fun observe(owner: LifecycleOwner?, observer: Observer<AsyncResult<T>>?) {
        super.observe(owner, observer)
        this.observer = observer
    }

    override fun onActive() {
        super.onActive()

        job = launch(UI) {
            try {
                val result = async(context + CommonPool) {
                    exec()
                }.await()

                value = AsyncResult.Success(result)
            } catch (e: CancellationException) {
                e.printStackTrace()
            } catch (e: Exception) {
                value = AsyncResult.Error(e)
            }

            removeObserver(observer)
        }
    }

    override fun onInactive() {
        super.onInactive()
        job?.cancel()
    }
}


