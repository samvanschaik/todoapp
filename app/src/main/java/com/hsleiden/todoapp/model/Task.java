package com.hsleiden.todoapp.model;

import java.util.Date;

public class Task {

    private int taskId;
    private String taskName;
    private Date taskDate;
    private int taskPriority;

    public Task(int taskId, String taskName, Date taskDate, int taskPriority) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskPriority = taskPriority;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
