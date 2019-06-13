package com.hsleiden.todoapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hsleiden.todoapp.model.Task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.ItemClickListener, Serializable  {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    TaskRecyclerViewAdapter adapter;

    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = new Intent(this, NewTaskActivity.class);

        //------
        // Floating Action Button Creation and onClick handling.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        // todo create a little example of a task for users.
        Task testTask = new Task("testTast1",
                new Date(2000, 1, 1),
                1);
        tasks.add(testTask);

        // ------
        // Recycler view Creation
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskRecyclerViewAdapter(this, tasks);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String taskName = data.getStringExtra("taskName");
                int taskDateYear= data.getIntExtra("taskDateYear", 0);
                int taskDateMonth = data.getIntExtra("taskDateMonth", 0 );
                int taskDateDay = data.getIntExtra("taskDateDay", 0 );
                int taskPriority = data.getIntExtra("taskPriority", 0);

                // todo clean this bit up a lil'
                Date taskDate = new Date(taskDateYear, taskDateMonth, taskDateDay);
                Task newTask = new Task(taskName, taskDate, taskPriority);
                tasks.add(newTask);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
