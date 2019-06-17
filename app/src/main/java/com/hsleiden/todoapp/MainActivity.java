package com.hsleiden.todoapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hsleiden.todoapp.model.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.ItemClickListener, Serializable  {
    public TaskRecyclerViewAdapter adapter;
    private int sortedState = 0; // 0 implies unsorted, 1 implies sorted by date, 2 by priority
    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fire base handling;
//        DatabaseReference reference = Utils.getDatabase().getReference();
        DatabaseReference reference = Utils.getDatabase().getReference().child("tasks");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasks.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Task task = ds.getValue(Task.class);
                    tasks.add(task);
                    adapter.notifyDataSetChanged();
                }

                // Sort tasks on start up
                if(sortedState == 0){
                    // todo this gets called on data change for some reason, still.
                    tasks.sort(Comparator.comparing(Task::getTaskPriority).reversed());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), getString(R.string.sorted_priority), Toast.LENGTH_SHORT).show();
                    sortedState = 2;
                }

                // todo re sort new data in view.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //todo Log Database error.
            }
        };
        reference.addValueEventListener(valueEventListener);

        // Recycler view Creation
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaskRecyclerViewAdapter(this, tasks);
        adapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        // FAB Add Task
        final Intent intent = new Intent(this, NewTaskActivity.class);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        // FAB Sort Tasks
        FloatingActionButton fabSort = findViewById(R.id.fabSort);
        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (sortedState) {
                    case 0:
                        tasks.sort(Comparator.comparing(Task::getTaskPriority).reversed());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), getString(R.string.sorted_priority), Toast.LENGTH_SHORT).show();
                        sortedState = 2;
                        break;

                    case 1:
                        tasks.sort(Comparator.comparing(Task::getTaskPriority).reversed());
                        Toast.makeText(getApplicationContext(), getString(R.string.sorted_priority), Toast.LENGTH_SHORT).show();
                        sortedState = 2;
                        adapter.notifyDataSetChanged();
                        break;

                    case 2:
                        tasks.sort(Comparator.comparing(Task::getTaskDate));
                        Toast.makeText(getApplicationContext(), getString(R.string.sorted_date), Toast.LENGTH_SHORT).show();
                        sortedState = 1;
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });


        // Delete / Complete item on swipe.
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, getString(R.string.task_completed), Toast.LENGTH_SHORT).show();
                int position = viewHolder.getAdapterPosition();
                reference.child(tasks.get(position).getTaskName()).removeValue();
                tasks.remove(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent myIntent = new Intent(this, EditTaskActivity.class);
        myIntent.putExtra("taskName",tasks.get(position).getTaskName());
        startActivity(myIntent);
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
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
