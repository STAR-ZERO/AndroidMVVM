package com.star_zero.example.androidmvvm.presentation.tasks

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.databinding.ActivityTasksBinding
import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity
import com.star_zero.example.androidmvvm.presentation.shared.view.BaseActivity
import com.star_zero.example.androidmvvm.presentation.tasks.adapter.ItemTaskViewModel
import com.star_zero.example.androidmvvm.presentation.tasks.adapter.TasksAdapter
import com.star_zero.example.androidmvvm.utils.extension.observe
import dagger.android.AndroidInjection
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

open class TasksActivity : BaseActivity() {

    private lateinit var binding: ActivityTasksBinding

    lateinit var viewModel: TasksViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter: TasksAdapter = TasksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TasksViewModel::class.java)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        subscribe()

        setSupportActionBar(binding.toolbar)

        binding.recyclerTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerTasks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerTasks.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    // ----------------------
    // EventBus subscribe (ItemTaskViewModel)
    // ----------------------

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClickTaskItem(event: ItemTaskViewModel.ClickTaskItemEvent) {
        val intent = AddEditTaskActivity.createIntent(this, event.task)
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChangeCompleteState(event: ItemTaskViewModel.ChangeCompleteStateEvent) {
        viewModel.changeCompleteState(event.task, event.completed)
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private fun subscribe() {
        viewModel.tasks().observe(this) {
            adapter.setTasks(it)
        }

        viewModel.clickNewTask().observe(this) {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startActivity(intent)
        }

        viewModel.errorMessage().observe(this) {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() }
        }
    }

}