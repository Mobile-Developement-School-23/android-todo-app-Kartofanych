package com.example.todoapp.ui.view.taskList

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UiState
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.ui.view.taskList.listAdapter.DealsAdapter
import com.example.todoapp.ui.view.taskList.listAdapter.OnItemListener
import com.example.todoapp.ui.view.taskList.listAdapter.SwipeCallbackInterface
import com.example.todoapp.ui.view.taskList.listAdapter.SwipeHelper
import com.example.todoapp.utils.internetConnection.ConnectivityObserver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TasksFragmentViewController(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentTasksBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
    private val layoutInflater: LayoutInflater
) {
    private var internetState = viewModel.status.value
    private val adapter: DealsAdapter get() = views { recycler.adapter as DealsAdapter }

    fun setUpViews() {
        setUpUI()
        setUpViewModel()
    }

    private fun setUpUI() {
        views {
            floatingNewTask.setOnClickListener {
                val action = TasksFragmentDirections.actionManageComposeTask(null)
                navController.navigate(action)
            }

            recycler.adapter = DealsAdapter(object : OnItemListener {
                override fun onItemClick(id: String) {
                    val action = TasksFragmentDirections.actionManageComposeTask(id)
                    navController.navigate(action)
                }

                override fun onCheckClick(todoItem: TodoItem) {
                    viewModel.updateItem(
                        todoItem.copy(
                            done = !todoItem.done
                        )
                    )
                }
            })
            val helper = SwipeHelper(object : SwipeCallbackInterface {
                override fun onDelete(todoItem: TodoItem) {
                    viewModel.deleteItem(todoItem)
                    showSnackbar(todoItem)
                }

                override fun onChangeDone(todoItem: TodoItem) {
                    viewModel.updateItem(
                        todoItem.copy(
                            done = !todoItem.done
                        )
                    )
                }
            }, context)
            helper.attachToRecyclerView(recycler)

            visible.setOnClickListener {
                viewModel.changeMode()
                if (viewModel.visibility.value) {
                    visible.setImageResource(R.drawable.ic_invisible)
                } else {
                    visible.setImageResource(R.drawable.ic_visible)
                }
            }

            refresher.setOnRefreshListener {
                viewModel.loadNetworkList()
                refresher.isRefreshing = false
            }

            floatingSettings.setOnClickListener {
                val action = TasksFragmentDirections.actionSetting()
                navController.navigate(action)
            }
        }
    }

    private fun showSnackbar(todoItem: TodoItem) {
        val snackbar = Snackbar.make(binding.recycler, "", Snackbar.LENGTH_INDEFINITE)
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE

        val customize = layoutInflater.inflate(R.layout.custom_snackbar, null)
        snackbar.view.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout
        val timerText = customize.findViewById<TextView>(R.id.timer)
        val timerTitle = customize.findViewById<TextView>(R.id.title)
        timerTitle.text = """Отменить удаление задачи "${todoItem.text}"?"""
        val cancel = customize.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            viewModel.addItem(todoItem)
            snackbar.dismiss()
        }
        snackBarLayout.addView(customize, 0)
        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000 + 1).toString()
            }

            override fun onFinish() {
                snackbar.dismiss()
            }
        }
        timer.start()

        snackbar.show()
    }

    private fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.status.collectLatest {
                    updateStatusUI(it)
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.visibility.collectLatest { visibilityState ->
                    updateStateUI(visibilityState)
                }
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countComplete.collectLatest {
                    updateCounter(it)
                }
            }
        }

        internetState = viewModel.status.value
    }

    private suspend fun updateStateUI(visibilityState: Boolean) {
        viewModel.data.collect { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (visibilityState) {
                        adapter.submitList(uiState.data
                            .sortedWith(compareBy<TodoItem, Long?>(nullsLast()) { it.deadline?.time }
                                .thenBy { it.dateCreation.time }))
                    } else {
                        adapter.submitList(uiState.data.filter { !it.done }
                            .sortedWith(compareBy<TodoItem, Long?>(nullsLast()) { it.deadline?.time }
                                .thenBy { it.dateCreation.time }))
                    }
                    views {
                        recycler.visibility = View.VISIBLE
                        loading.visibility = View.GONE
                    }
                }

                is UiState.Error -> Log.d("1", uiState.cause)
                is UiState.Start -> {
                    views {
                        recycler.visibility = View.GONE
                        loading.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateStatusUI(status: ConnectivityObserver.Status) {
        views {
            when (status) {
                ConnectivityObserver.Status.Available -> {
                    connection.imageTintList =
                        AppCompatResources.getColorStateList(context, R.color.green)
                    if (internetState != status) {
                        Toast.makeText(context, "Connected! Merging data...", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.loadNetworkList()
                    }
                }

                else -> {
                    connection.imageTintList =
                        AppCompatResources.getColorStateList(context, R.color.red)

                    if (internetState != status) {
                        Toast.makeText(context, "Internet Lost!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        internetState = status
    }

    private fun updateCounter(count: Int) {
        views {
            numberDone.text = "Выполнено - $count"
        }
    }

    private fun <T : Any> views(block: FragmentTasksBinding.() -> T): T = binding.block()

}
