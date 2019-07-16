package com.hsleiden.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.firebase.database.DatabaseReference;
import com.hsleiden.todoapp.model.Task;

public class NewTaskActivity extends AppCompatActivity {

    /* New task activity is a simple form-based menu that allows a user to fill the wanted
    * fields of his new task. The task will then be displayed in the MainActivity and stored on
    * the Firebase backend.*/

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
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        button.setOnClickListener(view -> {
            view.startAnimation(buttonClick);
            if(taskNameField.getText().length() == 0){
                taskNameField.setError(getString(R.string.task_name_error));
            } else {
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
