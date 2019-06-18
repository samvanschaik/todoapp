package com.hsleiden.todoapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hsleiden.todoapp.model.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import static com.hsleiden.todoapp.SortedState.*;

// TODO: Consider moving this out of the class.
enum SortedState{PRIORITY, DATE, NONE}

// TODO: Split Database logic, UI creation and App logic into different classes where possible.
public class MainActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.ItemClickListener {
    public TaskRecyclerViewAdapter adapter;
    private static DatabaseReference reference = Utils.getDatabase().getReference().child("tasks");
    private SortedState sortedState = NONE; // 0 implies unsorted, 1 implies sorted by date, 2 by priority
    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        createToolbar();

        startFirebase();

        createSortButton();
        createAddButton();
        createRecyclerView();

        // TODO: Make this not happen every time the user returns to this screen.
        createSwipeHint();

        adapter.notifyDataSetChanged();
    }

    private void createRecyclerView() {
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

        // Delete / Complete item on swipe.
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this,
                        getString(R.string.task_completed),
                        Toast.LENGTH_SHORT).show();
                int position = viewHolder.getAdapterPosition();
                reference.child(tasks.get(position).getTaskName()).removeValue();
                tasks.remove(position);
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }



    /* Creates toast that explains task completion functionality to the user.*/
    private void createSwipeHint() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                getString(R.string.tutorial_1),
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /* Handles synchronization with the back end.*/
    private void startFirebase() {
        ValueEventListener valueEventListener = new ValueEventListener() {

            /*Creates a task for every task in the list for every task that exists in Firebase*/
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasks.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Task task = ds.getValue(Task.class);
                    tasks.add(task);
                    adapter.notifyDataSetChanged();
                }

                // Sorts tasks on start up
                if (sortedState == NONE) {
                    // TODO: this gets called on data change for some reason, still.
                    sortTasksByPriority();
                }

                // TODO: Automatically sort data when added in view.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: Handle error.
            }
        };
        reference.addValueEventListener(valueEventListener);
    }

    /* Creates the FAB that allows users to add a task to the app. */
    private void createAddButton() {
        final Intent intent = new Intent(this, NewTaskActivity.class);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> startActivity(intent));
    }

    /* Creates the FAB that allows users to sort their tasks */
    private void createSortButton() {
        FloatingActionButton fabSort = findViewById(R.id.fabSort);
        fabSort.setOnClickListener(view -> {
            switch (sortedState) {
                case NONE:
                    sortTasksByPriority();
                    break;

                case DATE:
                    sortTasksByPriority();
                    break;

                case PRIORITY:
                    sortTasksByDate();
                    break;
            }
        });
    }

    private void sortTasksByPriority(){
        tasks.sort(Comparator.comparing(Task::getTaskPriority).reversed());
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), getString(R.string.sorted_priority), Toast.LENGTH_SHORT).show();
        sortedState = PRIORITY;
    }

    private void sortTasksByDate(){
        tasks.sort(Comparator.comparing(Task::getTaskDate));
        Toast.makeText(getApplicationContext(), getString(R.string.sorted_date), Toast.LENGTH_SHORT).show();
        sortedState = DATE;
        adapter.notifyDataSetChanged();
    }

    /* Opens the Edit Task screen when a user clicks on a task. */
    @Override
    public void onItemClick(View view, int position) {
        Intent myIntent = new Intent(this, EditTaskActivity.class);
        myIntent.putExtra("taskName", tasks.get(position).getTaskName());
        startActivity(myIntent);
    }

    /* Creates toolbar that allows user to access the settings menu. */
    /* TODO: Add user profile access here when users are implemented.*/
    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
