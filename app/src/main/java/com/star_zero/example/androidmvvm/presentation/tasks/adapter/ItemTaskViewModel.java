package com.star_zero.example.androidmvvm.presentation.tasks.adapter;

import android.databinding.ObservableField;
import android.view.View;
import android.widget.CompoundButton;

import com.star_zero.example.androidmvvm.domain.task.Task;

import org.greenrobot.eventbus.EventBus;

public class ItemTaskViewModel {

    public final ObservableField<Task> task = new ObservableField<>();

    void setTask(Task task) {
        this.task.set(task);
    }

    public void clickItem(View view) {
        EventBus.getDefault().post(new ClickTaskItemEvent(task.get()));
    }

    public void changeCheckComplete(CompoundButton view, boolean isChecked) {
        if (isChecked != task.get().isCompleted()) {
            EventBus.getDefault().post(new ChangeCompleteStateEvent(task.get(), isChecked));
        }
    }

    // ----------------------
    // EventBus event class
    // ----------------------

    public static class ClickTaskItemEvent {
        public final Task task;

        ClickTaskItemEvent(Task task) {
            this.task = task;
        }
    }

    public static class ChangeCompleteStateEvent {
        public final Task task;
        public final boolean completed;

        ChangeCompleteStateEvent(Task task, boolean completed) {
            this.task = task;
            this.completed = completed;
        }
    }
}
