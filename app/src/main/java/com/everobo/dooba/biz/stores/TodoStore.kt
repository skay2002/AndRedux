package com.everobo.dooba.biz.stores

import android.text.TextUtils
import android.util.Log

import com.squareup.otto.Subscribe

import com.everobo.dooba.biz.stores.model.Todo
import com.everobo.dooba.core.aciton.ArrayMapAction
import com.everobo.dooba.biz.actions.TodoTask
import com.everobo.dooba.core.store.Store
import java.util.ArrayList


object TodoStore : Store<ArrayMapAction>() {

    private val todos: MutableList<Todo>
    private var lastDeleted: Todo? = null

    fun getTodos(): List<Todo> {
        return todos
    }

    fun canUndo(): Boolean {
        return lastDeleted != null
    }

    init {
        todos = ArrayList<Todo>()
    }


    @Subscribe
    override fun onAction(action: ArrayMapAction) {
        var message = ""
        var id: Long = 0
        if (containsKey(action, TodoTask.KEY_MESSAGE)) {
            message = action
                    .datas[TodoTask.KEY_MESSAGE].toString().trim { it <= ' ' }
        }
        if (containsKey(action, TodoTask.KEY_ID)) {
            id = action.datas[TodoTask.KEY_ID] as Long
        }

        when (action.type) {
            TodoTask.TODO_CREATE -> if (!TextUtils.isEmpty(message)) {
                create(message)
                emitStoreChange()
            }
            TodoTask.TODO_TOGGLE_COMPLETE_ALL -> {
                updateCompleteAll()
                emitStoreChange()
            }
            TodoTask.TODO_UNDO_COMPLETE -> {
                updateComplete(id, false)
                emitStoreChange()
            }
            TodoTask.TODO_COMPLETE -> {
                updateComplete(id, true)
                emitStoreChange()
            }
            TodoTask.TODO_UPDATE_TEXT -> {
                updateText(id, message)
                emitStoreChange()
            }
            TodoTask.TODO_DESTROY -> {
                destroy(id)
                emitStoreChange()
            }
            TodoTask.TODO_DESTROY_COMPLETED -> {
                destroyCompleted()
                emitStoreChange()
            }

            else ->
                // no op
                Log.e(TAG, "this store no handle it ...type:" + action.type)
        }
    }

    override fun changeEvent(): Store.StoreChangeEvent {
        return Store.StoreChangeEvent()
    }

    private fun containsKey(action: ArrayMapAction, key: String): Boolean {
        if (null == action.datas) {
            return false
        } else {
            return action.datas.containsKey(key)
        }
    }

    private fun updateText(id: Long, message: String) {
        val todo = getById(id)
        if (todo != null) {
            todo.message = message
        }
    }

    private fun destroyCompleted() {
        val iter = todos.iterator()
        while (iter.hasNext()) {
            val todo = iter.next()
            if (todo.completed) {
                iter.remove()
            }
        }
    }

    private fun updateCompleteAll() {
        if (areAllComplete()) {
            updateAllComplete(false)
        } else {
            updateAllComplete(true)
        }
    }

    private fun areAllComplete(): Boolean {
        for (todo in todos) {
            if (!todo.completed) {
                return false
            }
        }
        return true
    }

    private fun updateAllComplete(complete: Boolean) {
        for (todo in todos) {
            todo.completed = complete
        }
    }

    private fun updateComplete(id: Long, complete: Boolean) {
        val todo = getById(id)
        if (todo != null) {
            todo.completed = complete
        }
    }

    private fun create(text: String) {
        val id = System.currentTimeMillis()
        val todo = Todo(id, text, false)
        addElement(todo)
    }

    private fun destroy(id: Long) {
        val iter = todos.iterator()
        while (iter.hasNext()) {
            val todo = iter.next()
            if (todo.id == id) {
                lastDeleted = todo.clone()
                iter.remove()
                break
            }
        }
    }

    private fun getById(id: Long): Todo? {
        val iter = todos.iterator()
        while (iter.hasNext()) {
            val todo = iter.next()
            if (todo.id == id) {
                return todo
            }
        }
        return null
    }

    private fun addElement(clone: Todo) {
        todos.add(clone)
    }

    val TAG = TodoStore::class.java.simpleName

}
