package com.star_zero.example.androidmvvm.presentation.add_edit_task

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import com.star_zero.example.androidmvvm.R
import com.star_zero.example.androidmvvm.databinding.ActivityAddEditTaskBinding
import com.star_zero.example.androidmvvm.domain.task.Task
import com.star_zero.example.androidmvvm.presentation.shared.view.BaseActivity
import com.star_zero.example.androidmvvm.utils.extension.observe
import dagger.android.AndroidInjection
import javax.inject.Inject

class AddEditTaskActivity : BaseActivity() {

    companion object {

        private val ARGS_KEY_TASK = "task"

        fun createIntent(context: Context, task: Task): Intent {
            val intent = Intent(context, AddEditTaskActivity::class.java)
            intent.putExtra(ARGS_KEY_TASK, task)
            return intent
        }
    }

    private lateinit var binding: ActivityAddEditTaskBinding

    lateinit var viewModel: AddEditTaskViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityAddEditTaskBinding>(this, R.layout.activity_add_edit_task)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditTaskViewModel::class.java)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        subscribe()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        if (intent.hasExtra(ARGS_KEY_TASK)) {
            viewModel.setTask(intent.getParcelableExtra<Task>(ARGS_KEY_TASK))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_edit_task, menu)
        menu.findItem(R.id.menu_delete).isVisible = viewModel.isSaved
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_delete -> {
                viewModel.deleteTask()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private fun subscribe() {
        viewModel.errorMessage().observe(this) {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() }
        }

        viewModel.successSaveTask().observe(this) { finish() }

        viewModel.successDeleteTask().observe(this) { finish() }
    }

}
