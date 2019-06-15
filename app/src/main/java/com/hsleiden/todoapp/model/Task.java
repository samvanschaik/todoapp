package com.hsleiden.todoapp.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private String taskName;
    private String taskDate;
    private int taskPriority;

    public Task(String taskName, String taskDate, int taskPriority) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskPriority = taskPriority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }
}
