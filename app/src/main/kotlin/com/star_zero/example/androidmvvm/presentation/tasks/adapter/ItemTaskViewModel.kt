package com.star_zero.example.androidmvvm.presentation.tasks.adapter

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import android.widget.CompoundButton

import com.android.databinding.library.baseAdapters.BR
import com.star_zero.example.androidmvvm.domain.task.Task

import org.greenrobot.eventbus.EventBus

class ItemTaskViewModel : BaseObservable() {

    // ----------------------
    // Binding
    // ----------------------

    @get:Bindable
    var task: Task? = null
        set(task) {
            field = task
            notifyPropertyChanged(BR.task)
        }

    fun clickItem(@Suppress("UNUSED_PARAMETER") view: View) {
        val task = this.task ?: return
        EventBus.getDefault().post(ClickTaskItemEvent(task))
    }

    fun changeCheckComplete(@Suppress("UNUSED_PARAMETER") view: CompoundButton, isChecked: Boolean) {
        val task = this.task ?: return
        if (isChecked != task.completed) {
            EventBus.getDefault().post(ChangeCompleteStateEvent(task, isChecked))
        }
    }

    // ----------------------
    // EventBus event class
    // ----------------------

    class ClickTaskItemEvent(val task: Task)

    class ChangeCompleteStateEvent(val task: Task, val completed: Boolean)
}
