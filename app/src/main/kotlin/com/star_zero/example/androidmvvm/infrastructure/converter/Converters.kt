package com.star_zero.example.androidmvvm.infrastructure.converter

import android.arch.persistence.room.TypeConverter
import com.star_zero.example.androidmvvm.domain.task.TaskId
import org.joda.time.DateTime

class Converters {
    companion object {

        @JvmStatic
        @TypeConverter
        fun toMillis(dateTime: DateTime): Long {
            return dateTime.millis
        }

        @JvmStatic
        @TypeConverter
        fun toDateTime(millis: Long): DateTime {
            return DateTime(millis)
        }

        @JvmStatic
        @TypeConverter
        fun toString(id: TaskId): String {
            return id.value
        }

        @JvmStatic
        @TypeConverter
        fun toTaskId(value: String): TaskId {
            return TaskId(value)
        }
    }
}

