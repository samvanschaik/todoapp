package com.hsleiden.todoapp.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private String taskName;
    private Date taskDate;
    private int taskPriority;

    public Task(String taskName, Date taskDate, int taskPriority) {
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

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }
}
