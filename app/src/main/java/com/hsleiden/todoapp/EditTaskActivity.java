package com.hsleiden.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsleiden.todoapp.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {
    Task task = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Intent myIntent = getIntent();
        String taskName = myIntent.getStringExtra("taskName");

        EditText taskNameField = findViewById(R.id.taskNameField);
        DatePicker taskDatePicker = findViewById(R.id.taskDatePicker);
        NumberPicker taskPriorityPicker = findViewById(R.id.taskNumberPicker);
        taskPriorityPicker.setMinValue(1);
        taskPriorityPicker.setMaxValue(9);

        DatabaseReference reference = Utils.getDatabase().getReference().child("tasks");
//        DatabaseReference taskReference = firebaseDatabase.getReference().child("tasks").child(taskName);

        reference.child(taskName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                task = snapshot.getValue(Task.class);

                taskNameField.setText(task.getTaskName());
                taskNameField.setEnabled(false);

                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                LocalDate d = LocalDate.parse(task.getTaskDate(), formatter);
                taskDatePicker.updateDate(d.getYear(), d.getMonthValue(), d.getDayOfMonth());

                taskPriorityPicker.setValue(task.getTaskPriority());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // todo logging
            }
        });

        Button button = findViewById(R.id.taskCreateButton);
        button.setText(getString(R.string.edit_your_task));

        final Intent intent = new Intent(this, MainActivity.class);

        button.setOnClickListener(view -> {
            if(taskNameField.getText().length() == 0){
                taskNameField.setError(getString(R.string.task_name_error));
            } else {
                reference.child(task.getTaskName()).setValue(
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
