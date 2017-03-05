package com.star_zero.example.androidmvvm.presentation.tasks.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.databinding.ItemTaskBinding;
import com.star_zero.example.androidmvvm.domain.task.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private List<Task> tasks;

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksAdapter.ViewHolder holder, int position) {
        holder.setTask(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        if (tasks == null) return 0;
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTaskBinding binding;

        private ItemTaskViewModel viewModel;

        ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);

            viewModel = new ItemTaskViewModel();
            binding.setViewModel(viewModel);
        }

        void setTask(Task task) {
            viewModel.setTask(task);
            binding.executePendingBindings();
        }
    }
}
