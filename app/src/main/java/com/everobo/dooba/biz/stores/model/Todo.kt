package com.everobo.dooba.biz.stores.model

class Todo(var id: Long, var message: String, var completed: Boolean) {

    fun clone(): Todo {
        return Todo(this.id, this.message, this.completed)
    }
}
