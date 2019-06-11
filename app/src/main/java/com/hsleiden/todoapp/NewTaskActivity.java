package com.hsleiden.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        NumberPicker np = (NumberPicker) findViewById(R.id.taskNumberPicker);
        np.setMinValue(1);
        np.setMaxValue(9);

        Button b = findViewById(R.id.taskCreateButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("HI STREM");
            }
        });
    }
}
