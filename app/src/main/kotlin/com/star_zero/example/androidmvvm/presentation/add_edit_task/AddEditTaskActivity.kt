package com.star_zero.example.androidmvvm.presentation.add_edit_task

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
import io.reactivex.disposables.CompositeDisposable
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

    private val disposables = CompositeDisposable()

    @Inject
    lateinit var viewModel: AddEditTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityAddEditTaskBinding>(this, R.layout.activity_add_edit_task)

        appComponent.inject(this)
        binding.viewModel = viewModel

        subscribe()
        viewModel.onCreate()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        if (intent.hasExtra(ARGS_KEY_TASK)) {
            viewModel.setTask(intent.getParcelableExtra<Task>(ARGS_KEY_TASK))
        }

        viewModel.restoreState(savedInstanceState)
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        disposables.clear()
        super.onDestroy()
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

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    // ----------------------
    // Subscribe
    // ----------------------

    private fun subscribe() {
        disposables.add(viewModel.successSaveTask.subscribe {
            finish()
        })

        disposables.add(viewModel.successDeleteTask.subscribe {
            finish()
        })

        disposables.add(viewModel.errorMessage.subscribe { resId ->
            Snackbar.make(binding.root, resId!!, Snackbar.LENGTH_LONG).show()
        })
    }

}
