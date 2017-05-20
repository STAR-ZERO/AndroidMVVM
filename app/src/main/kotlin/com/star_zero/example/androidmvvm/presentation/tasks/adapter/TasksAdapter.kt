package com.star_zero.example.androidmvvm.presentation.tasks.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.databinding.ItemTaskBinding
import com.star_zero.example.androidmvvm.domain.task.Task

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private var tasks: List<Task>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksAdapter.ViewHolder, position: Int) {
        val tasks = this.tasks ?: return
        holder.setTask(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemTaskBinding = DataBindingUtil.bind<ItemTaskBinding>(itemView)

        private val viewModel: ItemTaskViewModel = ItemTaskViewModel()

        init {
            binding.viewModel = viewModel
        }

        fun setTask(task: Task) {
            viewModel.task = task
            binding.executePendingBindings()
        }
    }
}
