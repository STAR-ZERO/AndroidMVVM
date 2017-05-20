package com.star_zero.example.androidmvvm.presentation.tasks

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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

open class TasksActivity : BaseActivity() {

    private lateinit var binding: ActivityTasksBinding

    private val subscriptions = CompositeSubscription()

    @Inject
    lateinit var viewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks)

        appComponent.inject(this)
        binding.viewModel = viewModel

        subscribe()
        viewModel.onCreate()

        setSupportActionBar(binding.toolbar)

        binding.recyclerTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerTasks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        viewModel.onStart()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        subscriptions.clear()
        super.onDestroy()
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
        subscriptions.add(viewModel.clickNewTask.subscribe {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startActivity(intent)
        })

        subscriptions.add(viewModel.errorMessage.subscribe { resId ->
            Snackbar.make(binding.root, resId, Snackbar.LENGTH_LONG).show()
        })
    }

}