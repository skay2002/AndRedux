package com.everobo.dooba.biz.actions;

import com.everobo.dooba.biz.stores.TodoStore;
import com.everobo.dooba.biz.stores.model.Todo;
import com.everobo.dooba.core.ConstID;
import com.everobo.dooba.core.Dispatcher;
import com.everobo.dooba.core.Task;
import com.everobo.dooba.core.aciton.ActionCreator;
import com.everobo.dooba.core.aciton.ArrayMapAction;


public class TodoTask extends Task.TaskImpl<TodoTask.TodoActionCreator,TodoStore> implements ConstID {
    public static final String TODO_CREATE = "TODO_CREATE";
    public static final String TODO_COMPLETE = "TODO_COMPLETE";
    public static final String TODO_DESTROY = "TODO_DESTROY";
    public static final String TODO_DESTROY_COMPLETED = "TODO_DESTROY_COMPLETED";
    public static final String TODO_TOGGLE_COMPLETE_ALL = "TODO_TOGGLE_COMPLETE_ALL";
    public static final String TODO_UNDO_COMPLETE = "TODO_UNDO_COMPLETE";
    public static final String TODO_UPDATE_TEXT = "TODO_UPDATE_TEXT";

    public TodoTask() {
        super(
                TodoActionCreator.getInstance(Dispatcher.getInstance()) ,
                TodoStore.getInstance()
        );
    }

    static public class TodoActionCreator implements ActionCreator<ArrayMapAction> {
        private static TodoActionCreator instance;
        private Dispatcher dispatcher;

        private TodoActionCreator(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public static TodoActionCreator getInstance(Dispatcher dispatcher) {

            if (null == instance) {
                instance = new TodoActionCreator(dispatcher);
            }
            return instance;
        }


        @Override public ArrayMapAction createAction(String type, Object... params) {
            ArrayMapAction action = new ArrayMapAction();
            action.init(type, params);
            return action;
        }



        public void create(String text) {
            dispatcher.dispatch(
                    createAction(
                            TodoTask.TODO_CREATE,
                            TodoTask.KEY_MESSAGE, text
                    ));
        }

        public void updateText(long id, String text) {
            dispatcher.dispatch(
                    createAction(
                            TodoTask.TODO_UPDATE_TEXT,
                            TodoTask.KEY_ID, id,
                            TodoTask.KEY_MESSAGE, text
                    ));
        }

        public void toggleComplete(Todo todo) {
            String type = todo.getCompleted() ?
                    TodoTask.TODO_UNDO_COMPLETE :
                    TodoTask.TODO_COMPLETE;
            dispatcher.dispatch(
                    createAction(type,
                            TodoTask.KEY_ID, todo.getId()
                    ));
        }

        public void toggleCompleteAll() {
            dispatcher.dispatch(createAction(TodoTask.TODO_TOGGLE_COMPLETE_ALL));
        }

        public void destroy(long id) {
            dispatcher.dispatch(createAction(TodoTask.TODO_DESTROY,
                    TodoTask.KEY_ID, id));
        }

        public void destroyCompleted() {
            dispatcher.dispatch(createAction(TodoTask.TODO_DESTROY_COMPLETED));
        }
    }
}
