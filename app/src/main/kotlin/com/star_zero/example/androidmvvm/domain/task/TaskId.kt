package com.star_zero.example.androidmvvm.domain.task

import android.os.Parcel
import android.os.Parcelable
import com.star_zero.example.androidmvvm.domain.Identifier

data class TaskId(override val value: String) : Identifier, Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<TaskId> = object : Parcelable.Creator<TaskId> {
            override fun createFromParcel(source: Parcel): TaskId = TaskId(source)
            override fun newArray(size: Int): Array<TaskId?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(value)
    }
}