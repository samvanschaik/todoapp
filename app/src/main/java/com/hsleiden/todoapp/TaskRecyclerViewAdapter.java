package com.hsleiden.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hsleiden.todoapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Task> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    TaskRecyclerViewAdapter(Context context, ArrayList<Task> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // Creates views when put on screen.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mData.get(position);
        holder.taskNameView.setText(task.getTaskName());
        holder.taskPriorityView.setText(String.valueOf(task.getTaskPriority()));

        String date = task.getTaskDate();
        holder.taskDateView.setText(date);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Recycles views when off screen.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskNameView;
        TextView taskDateView;
        TextView taskPriorityView;

        ViewHolder(View itemView) {
            super(itemView);
            taskNameView = itemView.findViewById(R.id.taskName);
            taskDateView = itemView.findViewById(R.id.taskDate);
            taskPriorityView = itemView.findViewById(R.id.taskPriority);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Task getItem(int id) {
        return mData.get(id);
    }

    // Allows us to listen for click activities.
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}