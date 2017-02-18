package com.everobo.dooba.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.everobo.dooba.R;
import com.squareup.otto.Subscribe;
import com.everobo.dooba.core.store.Store;
import com.everobo.dooba.ui.adapter.TodoAdapter;
import com.everobo.dooba.core.Task;
import com.everobo.dooba.biz.stores.TodoStore;

public class MainActivity extends AppCompatActivity {

    private RecyclerView dataList;
    private AppCompatEditText editor;
    private AppCompatButton add;
    private AppCompatButton completeAll;
    private AppCompatButton destroyComplete;

    private TodoAdapter todoAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Task.todo().init(this);
        initViews();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Task.todo().uninit(this);
    }

    @Subscribe public void onStoreChange(Store.StoreChangeEvent event) {
        if (event != null) {
            reader(Task.todo().store());
        }
    }

    private void initViews() {
        dataList = (RecyclerView) findViewById(R.id.rv);
        add = (AppCompatButton) findViewById(R.id.add_todo);
        completeAll = (AppCompatButton) findViewById(R.id.complete_all);
        destroyComplete = (AppCompatButton) findViewById(R.id.destroy_complete);
        editor = (AppCompatEditText) findViewById(R.id.editor);

        add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Task.todo().action().create(editor.getText().toString());
            }
        });
        completeAll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Task.todo().action().toggleCompleteAll();
            }
        });
        destroyComplete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Task.todo().action().destroyCompleted();
            }
        });

        dataList.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(Task.todo().action(), R.layout.item_todo);
        dataList.setAdapter(todoAdapter);
    }

    private void reader(TodoStore store) {
        todoAdapter.setDatas(store.getTodos());
        todoAdapter.notifyDataSetChanged();
    }


}
