package com.everobo.dooba.biz.stores;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.otto.Subscribe;

import com.everobo.dooba.biz.stores.model.Todo;
import com.everobo.dooba.core.aciton.ArrayMapAction;
import com.everobo.dooba.biz.actions.TodoTask;
import com.everobo.dooba.core.store.Store;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TodoStore extends Store<ArrayMapAction> {

  private final List<Todo> todos;
  private Todo lastDeleted;

  public List<Todo> getTodos() {
    return todos;
  }

  public boolean canUndo() {
    return lastDeleted != null;
  }



  static final public String TAG = TodoStore.class.getSimpleName();

  protected TodoStore() {
    todos = new ArrayList<>();
  }

  private static TodoStore instance;

  public static TodoStore getInstance() {
    if (instance == null) {
      instance = new TodoStore();
    }
    return instance;
  }


  @Subscribe
  @Override public void onAction(ArrayMapAction action) {
    String message = "";
    long id = 0;
    if (containsKey(action, TodoTask.KEY_MESSAGE)) {
      message = action
              .getDatas().get(TodoTask.KEY_MESSAGE).
                      toString().trim();
    }
    if (containsKey(action, TodoTask.KEY_ID)) {
      id = (long) action.getDatas().get(TodoTask.KEY_ID);
    }

    switch (action.getType()) {
      case TodoTask.TODO_CREATE:
        if (!TextUtils.isEmpty(message)) {
          create(message);
          emitStoreChange();
        }
        break;
      case TodoTask.TODO_TOGGLE_COMPLETE_ALL:
        updateCompleteAll();
        emitStoreChange();
        break;
      case TodoTask.TODO_UNDO_COMPLETE:
        updateComplete(id, false);
        emitStoreChange();
        break;
      case TodoTask.TODO_COMPLETE:
        updateComplete(id, true);
        emitStoreChange();
        break;
      case TodoTask.TODO_UPDATE_TEXT:
        updateText(id, message);
        emitStoreChange();
        break;
      case TodoTask.TODO_DESTROY:
        destroy(id);
        emitStoreChange();
        break;
      case TodoTask.TODO_DESTROY_COMPLETED:
        destroyCompleted();
        emitStoreChange();
        break;

      default:
        // no op
        Log.e(TAG,"this store no handle it ...type:"+action.getType());
    }
  }

  @Override public StoreChangeEvent changeEvent() {
    return new StoreChangeEvent();
  }

  private boolean containsKey(ArrayMapAction action, String key) {
    if (null == action.getDatas()) {
      return false;
    }else {
      return action.getDatas().containsKey(key);
    }
  }

  private void updateText(long id, String message) {
    Todo todo = getById(id);
    if (todo != null) {
      todo.message = message;
    }
  }

  private void destroyCompleted() {
    Iterator<Todo> iter = todos.iterator();
    while (iter.hasNext()) {
      Todo todo = iter.next();
      if (todo.completed) {
        iter.remove();
      }
    }
  }

  private void updateCompleteAll() {
    if (areAllComplete()) {
      updateAllComplete(false);
    } else {
      updateAllComplete(true);
    }
  }

  private boolean areAllComplete() {
    for (Todo todo : todos) {
      if (!todo.completed) {
        return false;
      }
    }
    return true;
  }

  private void updateAllComplete(boolean complete) {
    for (Todo todo : todos) {
      todo.completed = complete;
    }
  }

  private void updateComplete(long id, boolean complete) {
    Todo todo = getById(id);
    if (todo != null) {
      todo.completed = complete;
    }
  }

  private void create(String text) {
    long id = System.currentTimeMillis();
    Todo todo = new Todo(id, text, false);
    addElement(todo);
  }

  private void destroy(long id) {
    Iterator<Todo> iter = todos.iterator();
    while (iter.hasNext()) {
      Todo todo = iter.next();
      if (todo.id == id) {
        lastDeleted = todo.clone();
        iter.remove();
        break;
      }
    }
  }

  private Todo getById(long id) {
    Iterator<Todo> iter = todos.iterator();
    while (iter.hasNext()) {
      Todo todo = iter.next();
      if (todo.id == id) {
        return todo;
      }
    }
    return null;
  }

  private void addElement(Todo clone) {
    todos.add(clone);
  }


}
