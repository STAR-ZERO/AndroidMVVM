package com.star_zero.example.androidmvvm.domain.task;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.star_zero.example.androidmvvm.domain.Identifier;

public class TaskId extends Identifier implements Parcelable {

    public TaskId(@NonNull String value) {
        super(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
    }

    private TaskId(Parcel in) {
        super(in.readString());
    }

    public static final Creator<TaskId> CREATOR = new Creator<TaskId>() {
        @Override
        public TaskId createFromParcel(Parcel source) {
            return new TaskId(source);
        }

        @Override
        public TaskId[] newArray(int size) {
            return new TaskId[size];
        }
    };
}
