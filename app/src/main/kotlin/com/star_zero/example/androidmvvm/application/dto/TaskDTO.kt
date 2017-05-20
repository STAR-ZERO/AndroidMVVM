package com.star_zero.example.androidmvvm.application.dto

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable
import com.star_zero.example.androidmvvm.BR
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.domain.task.TaskValidator

class TaskDTO() : BaseObservable(), Parcelable {

    var task: Task? = null
        private set

    @get:Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var description: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @get:Bindable
    var titleError: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.titleError)
        }

    @get:Bindable
    var descriptionError: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.descriptionError)
        }

    fun clearValidationErrors() {
        titleError = 0
        descriptionError = 0
    }

    fun setValidationErrors(errors: List<TaskValidator.ErrorType>) {
        errors.forEach { it ->
            when (it) {
                TaskValidator.ErrorType.TITLE_EMPTY -> titleError = R.string.validation_error_title_empty
                TaskValidator.ErrorType.TITLE_TOO_LONG -> titleError = R.string.validation_error_title_too_long
                TaskValidator.ErrorType.DESCRIPTION_TOO_LONG -> descriptionError = R.string.validation_error_description_too_long
            }
        }
    }

    companion object {
        @JvmStatic
        fun createFromTask(task: Task): TaskDTO {
            val dto = TaskDTO()
            dto.task = task
            dto.title = task.title
            dto.description = task.description
            return dto
        }

        @JvmField val CREATOR: Parcelable.Creator<TaskDTO> = object : Parcelable.Creator<TaskDTO> {
            override fun createFromParcel(source: Parcel): TaskDTO = TaskDTO(source)
            override fun newArray(size: Int): Array<TaskDTO?> = arrayOfNulls(size)
        }
    }

    private constructor(source: Parcel) : this() {
        task = source.readParcelable(Task::class.java.classLoader)
        title = source.readString()
        description = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.task, flags)
        dest.writeString(this.title)
        dest.writeString(this.description)
    }
}

