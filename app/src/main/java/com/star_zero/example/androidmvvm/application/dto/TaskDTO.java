package com.star_zero.example.androidmvvm.application.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.android.databinding.library.baseAdapters.BR;
import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.domain.task.TaskValidator;

import java.util.List;

public class TaskDTO extends BaseObservable implements Parcelable {

    private Task task;

    private String title;

    private String description;

    @StringRes
    private int titleError;

    @StringRes
    private int descriptionError;

    public TaskDTO() {
    }

    public Task getTask() {
        return task;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    @StringRes
    public int getTitleError() {
        return titleError;
    }

    public void setTitleError(@StringRes int titleError) {
        this.titleError = titleError;
        notifyPropertyChanged(BR.titleError);
    }

    @Bindable
    @StringRes
    public int getDescriptionError() {
        return descriptionError;
    }

    public void setDescriptionError(@StringRes int descriptionError) {
        this.descriptionError = descriptionError;
        notifyPropertyChanged(BR.descriptionError);
    }

    public static TaskDTO createFromTask(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.task = task;
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        return dto;
    }

    public void clearValidationErrors() {
        setTitleError(0);
        setDescriptionError(0);
    }

    public void setValidationErrors(List<TaskValidator.ErrorType> errors) {
        for (TaskValidator.ErrorType errorType : errors) {
            switch (errorType) {
                case TITLE_EMPTY:
                    setTitleError(R.string.validation_error_title_empty);
                    break;
                case TITLE_TOO_LONG:
                    setTitleError(R.string.validation_error_title_too_long);
                    break;
                case DESCRIPTION_TOO_LONG:
                    setDescriptionError(R.string.validation_error_description_too_long);
                    break;
            }
        }
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
        dest.writeParcelable(this.task, flags);
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    private TaskDTO(Parcel in) {
        this.task = in.readParcelable(Task.class.getClassLoader());
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<TaskDTO> CREATOR = new Parcelable.Creator<TaskDTO>() {
        @Override
        public TaskDTO createFromParcel(Parcel source) {
            return new TaskDTO(source);
        }

        @Override
        public TaskDTO[] newArray(int size) {
            return new TaskDTO[size];
        }
    };
}
