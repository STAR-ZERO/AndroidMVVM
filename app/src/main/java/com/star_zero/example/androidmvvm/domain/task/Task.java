package com.star_zero.example.androidmvvm.domain.task;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.star_zero.example.androidmvvm.domain.Entity;

import org.joda.time.DateTime;

public class Task extends Entity<TaskId> implements Parcelable {

    private String title;

    private String description;

    private boolean completed;

    private DateTime createdAt;

    protected Task(TaskId taskId) {
        super(taskId);
    }

    // ----------------------
    // Factory
    // ----------------------

    /**
     * @deprecated Use from persistent only.
     */
    public static Task createFromPersistent(TaskId taskId, String title, String description, boolean completed, DateTime createdAt) {
        Task task = new Task(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        task.setCreatedAt(createdAt);
        return task;
    }

    public static Task createNewTask(TaskId taskId, String title, String description) {
        Task task = new Task(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);
        task.setCreatedAt(DateTime.now());
        return task;
    }

    // ----------------------
    // Operation
    // ----------------------

    public void completeTask() {
        setCompleted(true);
    }

    public void activateTask() {
        setCompleted(false);
    }

    public void update(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    // ----------------------
    // Getter/Setter
    // ----------------------

    @Bindable
    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public boolean isCompleted() {
        return completed;
    }

    private void setCompleted(boolean completed) {
        this.completed = completed;
        notifyPropertyChanged(BR.completed);
    }

    @Bindable
    public DateTime getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    // ----------------------
    // Parcelable
    // ----------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte(this.completed ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.createdAt);
    }

    protected Task(Parcel in) {
        this((TaskId) in.readParcelable(TaskId.class.getClassLoader()));
        this.title = in.readString();
        this.description = in.readString();
        this.completed = in.readByte() != 0;
        this.createdAt = (DateTime) in.readSerializable();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
