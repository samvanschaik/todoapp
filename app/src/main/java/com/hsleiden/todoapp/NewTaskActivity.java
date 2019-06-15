package com.hsleiden.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsleiden.todoapp.model.Task;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        final NumberPicker taskPriority = findViewById(R.id.taskNumberPicker);
        taskPriority.setMinValue(1);
        taskPriority.setMaxValue(9);

        Button b = findViewById(R.id.taskCreateButton);

        final EditText taskName = findViewById(R.id.taskNameField);
        final DatePicker taskDate = findViewById(R.id.taskDatePicker);

        final Intent intent = new Intent(this, MainActivity.class);

        b.setOnClickListener(view -> {
            if(taskName.getText().length() == 0){
                taskName.setError("Your task needs a name!");
            } else {
                Task newTask = new Task(
                        taskName.getText().toString(),
                        taskDate.getYear() + "-" +
                                taskDate.getMonth() + "-" +
                                taskDate.getDayOfMonth(),
                        taskPriority.getValue());

                // todo newTask.getTaskName should be a unique identifier of some kind.
                reference.child("tasks").child(newTask.getTaskName()).setValue(newTask);

                startActivity(intent);
            }
        });
    }
}
