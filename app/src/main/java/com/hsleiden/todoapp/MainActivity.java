package com.hsleiden.todoapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsleiden.todoapp.model.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.ItemClickListener, Serializable  {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    TaskRecyclerViewAdapter adapter;

    //todo can probably make sortedstate less verbose
    private int sortedState = 0; // 0 implies unsorted, 1 implies sorted by date, 2 by priority
    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = new Intent(this, NewTaskActivity.class);

        // todo create a little example of a task for users.
//        Task testTask = new Task("Test Task 1",
//                "2000-01-01",
//                5);
//
//
//        Task testTask2 = new Task("Test Task 2",
//                "2001-01-01",
//                3);
//
//        Task testTask3 = new Task("Test Task 3",
//                "2002-01-01",
//                7);
//
//        tasks.add(testTask);
//        tasks.add(testTask2);
//        tasks.add(testTask3);

        // Fire base handling
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DatabaseReference allTasksReference = reference.child("tasks");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Task task = ds.getValue(Task.class);
                    tasks.add(task);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        allTasksReference.addListenerForSingleValueEvent(valueEventListener);

        // ------
        // Recycler view Creation
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaskRecyclerViewAdapter(this, tasks);
        adapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        //------
        // Floating Action Button Add Creation and onClick handling.
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        //------
        // Floating Action Button Sort Creation and onClick handling.
        FloatingActionButton fabSort = findViewById(R.id.fabSort);
        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (sortedState) {
                    case 0:
                        tasks.sort(Comparator.comparing(Task::getTaskPriority).reversed());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), getString(R.string.sorted_priority), Toast.LENGTH_LONG).show();
                        sortedState = 2;
                        break;

                    case 1:
                        tasks.sort(Comparator.comparing(Task::getTaskPriority));
                        Toast.makeText(getApplicationContext(), getString(R.string.sorted_priority), Toast.LENGTH_LONG).show();
                        sortedState = 2;
                        adapter.notifyDataSetChanged();
                        break;

                    case 2:
                        tasks.sort(Comparator.comparing(Task::getTaskDate));
                        Toast.makeText(getApplicationContext(), getString(R.string.sorted_date), Toast.LENGTH_LONG).show();
                        sortedState = 1;
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        // todo Could implement a user setting for default sorting
        // Sort tasks on start up
        if(sortedState == 0){
            tasks.sort(Comparator.comparing(Task::getTaskPriority).reversed());
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), getString(R.string.sorted_date), Toast.LENGTH_LONG).show();
            sortedState = 2;
        }


    }

    /* Todo, temporarily this method deletes a task on click. On click should later
    edit the task, and a seperate button for completing it should be added. */
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, getString(R.string.deleted_task) + adapter.getItem(position).getTaskName(), Toast.LENGTH_LONG).show();
        tasks.remove(position);
        adapter.notifyDataSetChanged();
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
                tasks.add(new Task(
                                data.getStringExtra("taskName"),
                                data.getStringExtra("taskDate"),
                                data.getIntExtra("taskPriority", 0)));

                adapter.notifyDataSetChanged();
            }
        }
    }
}
