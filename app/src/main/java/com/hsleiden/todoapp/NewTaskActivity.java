package com.hsleiden.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.hsleiden.todoapp.model.Task;

import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        final NumberPicker taskPriority = (NumberPicker) findViewById(R.id.taskNumberPicker);
        taskPriority.setMinValue(1);
        taskPriority.setMaxValue(9);

        Button b = (Button) findViewById(R.id.taskCreateButton);

        final EditText taskName = (EditText) findViewById(R.id.taskNameField);
        final DatePicker taskDate = (DatePicker) findViewById(R.id.taskDatePicker) ;

        final Intent intent = new Intent(this, MainActivity.class);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task newTask = new Task(taskName.getText().toString(),
                        new Date(taskDate.getYear(), taskDate.getMonth(), taskDate.getDayOfMonth()),
                        taskPriority.getValue());

                intent.putExtra("taskName", taskName.getText().toString());
                intent.putExtra("taskDateYear", taskDate.getYear());
                intent.putExtra("taskDateMonth", taskDate.getMonth());
                intent.putExtra("taskDateDay", taskDate.getDayOfMonth());
                intent.putExtra("taskPriority", taskPriority.getValue());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}