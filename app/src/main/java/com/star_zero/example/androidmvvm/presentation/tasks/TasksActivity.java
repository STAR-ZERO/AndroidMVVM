package com.star_zero.example.androidmvvm.presentation.tasks;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.databinding.ActivityTasksBinding;
import com.star_zero.example.androidmvvm.presentation.add_edit_task.AddEditTaskActivity;
import com.star_zero.example.androidmvvm.presentation.shared.view.BaseActivity;
import com.star_zero.example.androidmvvm.presentation.tasks.adapter.ItemTaskViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public class TasksActivity extends BaseActivity {

    private ActivityTasksBinding binding;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    TasksViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks);

        getAppComponent().inject(this);
        binding.setViewModel(viewModel);

        subscribe();
        viewModel.onCreate();

        setSupportActionBar(binding.toolbar);

        binding.recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        viewModel.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroy();
        subscriptions.clear();
        super.onDestroy();
    }

    // ----------------------
    // EventBus subscribe (ItemTaskViewModel)
    // ----------------------

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickTaskItem(ItemTaskViewModel.ClickTaskItemEvent event) {
        Intent intent = AddEditTaskActivity.createIntent(this, event.task);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeCompleteState(ItemTaskViewModel.ChangeCompleteStateEvent event) {
        viewModel.changeCompleteState(event.task, event.completed);
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private void subscribe() {
        subscriptions.add(viewModel.clickNewTask.subscribe(aVoid -> {
            Intent intent = new Intent(this, AddEditTaskActivity.class);
            startActivity(intent);
        }));

        subscriptions.add(viewModel.errorMessage.subscribe(resId -> {
            Snackbar.make(binding.getRoot(), resId, Snackbar.LENGTH_LONG).show();
        }));
    }

}
