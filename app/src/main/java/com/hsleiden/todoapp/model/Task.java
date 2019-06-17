package com.hsleiden.todoapp.model;

import java.io.Serializable;

public class Task implements Serializable {

    private String taskName;
    private String taskDate;
    private int taskPriority;

    public Task(String taskName, String taskDate, int taskPriority) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskPriority = taskPriority;
    }

    public Task(){}

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

}
