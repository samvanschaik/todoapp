package com.hsleiden.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.firebase.database.DatabaseReference;
import com.hsleiden.todoapp.model.Task;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        DatabaseReference reference = Utils.getDatabase().getReference();

        final NumberPicker taskPriorityPicker = findViewById(R.id.taskNumberPicker);
        taskPriorityPicker.setMinValue(1);
        taskPriorityPicker.setMaxValue(9);

        Button button = findViewById(R.id.taskCreateButton);

        final EditText taskNameField = findViewById(R.id.taskNameField);

        final DatePicker taskDatePicker = findViewById(R.id.taskDatePicker);
        taskDatePicker.setMinDate(System.currentTimeMillis()); // Ensures data is in future.

        final Intent intent = new Intent(this, MainActivity.class);

        button.setOnClickListener(view -> {
            if(taskNameField.getText().length() == 0){
                taskNameField.setError(getString(R.string.task_name_error));
            } else {
                // todo newTask.getTaskName should be a unique identifier of some kind.

                reference.child("tasks").child(taskNameField.getText().toString()).setValue(
                        new Task(
                        taskNameField.getText().toString(),
                        taskDatePicker.getYear() + "-" +
                                taskDatePicker.getMonth() + "-" +
                                taskDatePicker.getDayOfMonth(),
                        taskPriorityPicker.getValue()));

                startActivity(intent);
            }
        });
    }
}
