package com.star_zero.example.androidmvvm.infrastructure.task

import io.realm.RealmObject

open class TaskTable: RealmObject() {

    var id: String? = null

    var title: String? = null

    var description: String? = null

    var completed: Boolean = false

    var createdAt: Long = 0
}

