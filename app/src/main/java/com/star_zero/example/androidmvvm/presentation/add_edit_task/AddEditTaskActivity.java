package com.star_zero.example.androidmvvm.presentation.add_edit_task;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.star_zero.example.androidmvvm.R;
import com.star_zero.example.androidmvvm.databinding.ActivityAddEditTaskBinding;
import com.star_zero.example.androidmvvm.domain.task.Task;
import com.star_zero.example.androidmvvm.presentation.shared.view.BaseActivity;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public class AddEditTaskActivity extends BaseActivity {

    private static final String ARGS_KEY_TASK = "task";

    private ActivityAddEditTaskBinding binding;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    AddEditTaskViewModel viewModel;

    public static Intent createIntent(Context context, Task task) {
        Intent intent = new Intent(context, AddEditTaskActivity.class);
        intent.putExtra(ARGS_KEY_TASK, task);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_task);

        getAppComponent().inject(this);
        binding.setViewModel(viewModel);

        subscribe();
        viewModel.onCreate();

        setSupportActionBar(binding.toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(ARGS_KEY_TASK)) {
            viewModel.setTask(intent.getParcelableExtra(ARGS_KEY_TASK));
        }

        viewModel.restoreState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroy();
        subscriptions.clear();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_edit_task, menu);
        menu.findItem(R.id.menu_delete).setVisible(viewModel.isSaved());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_delete:
                viewModel.deleteTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        viewModel.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private void subscribe() {
        subscriptions.add(viewModel.successSaveTask.subscribe(aVoid -> {
            finish();
        }));

        subscriptions.add(viewModel.successDeleteTask.subscribe(aVoid -> {
            finish();
        }));

        subscriptions.add(viewModel.errorMessage.subscribe(resId -> {
            Snackbar.make(binding.getRoot(), resId, Snackbar.LENGTH_LONG).show();
        }));
    }
}
