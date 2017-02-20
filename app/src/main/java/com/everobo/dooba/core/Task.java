package com.everobo.dooba.core;

import com.everobo.dooba.core.aciton.ActionCreator;
import com.everobo.dooba.core.store.Store;
import com.everobo.dooba.biz.actions.TodoTask;


/**
 * Created by zhangchi on 2017/2/18.
 *
 * Task核心实现
 */

public class Task {

    static final private Task ins = new Task();

    static public Task use(){
        return ins;
    }

    static public class TaskImpl<A extends ActionCreator,S extends Store>{
        private A aciton;
        private S store;
        private Dispatcher dispatcher;

        protected TaskImpl(A aciton,S store){
            this.aciton = aciton;
            this.store = store;
        }

        public void init(Object view){
            try {
                store.register(view);
                dispatcher = Dispatcher.getInstance();
                dispatcher.register(store);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void uninit(Object view){
            try {
                dispatcher.unregister(store);
                store.unregister(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public A action(){return aciton;}
        public S store(){return store;}
    }


    static private TodoTask todo;

    static public TodoTask todo(){
        if(todo==null){
            todo = new TodoTask();
        }
        return todo;
    }


}

