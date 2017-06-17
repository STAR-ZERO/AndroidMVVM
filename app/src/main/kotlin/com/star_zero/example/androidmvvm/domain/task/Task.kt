package com.star_zero.example.androidmvvm.domain.task

import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable
import com.star_zero.example.androidmvvm.BR
import com.star_zero.example.androidmvvm.domain.Entity
import org.joda.time.DateTime

class Task private constructor(taskId: TaskId, title: String, description: String?, completed: Boolean, createdAt: DateTime) : Entity<TaskId>(taskId), Parcelable {

    @get:Bindable
    var title: String = title
        private set (value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var description: String? = description
        private set (value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @get:Bindable
    var completed: Boolean = completed
        private set (value) {
            field = value
            notifyPropertyChanged(BR.completed)
        }

    @get:Bindable
    var createdAt: DateTime = createdAt
        private set (value) {
            field = value
            notifyPropertyChanged(BR.createdAt)
        }

    fun completeTask() {
        this.completed = true
    }

    fun activateTask() {
        this.completed = false
    }

    fun update(title: String, description: String?) {
        this.title = title
        this.description = description
    }

    companion object {

        @JvmStatic
        @Deprecated("Use from persistent only.")
        fun createFromPersistent(taskId: TaskId, title: String, description: String?, completed: Boolean, createdAt: DateTime): Task {
            return Task(
                    taskId,
                    title,
                    description,
                    completed,
                    createdAt
            )
        }

        @JvmStatic
        fun createNewTask(taskId: TaskId, title: String, description: String?): Task {
            return Task(
                    taskId,
                    title,
                    description,
                    false,
                    DateTime.now()
            )
        }

        @JvmField val CREATOR: Parcelable.Creator<Task> = object : Parcelable.Creator<Task> {
            override fun createFromParcel(source: Parcel): Task = Task(source)
            override fun newArray(size: Int): Array<Task?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readParcelable<TaskId>(TaskId::class.java.classLoader),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readSerializable() as DateTime
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(id, 0)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeInt((if (completed) 1 else 0))
        dest.writeSerializable(createdAt)
    }
}

