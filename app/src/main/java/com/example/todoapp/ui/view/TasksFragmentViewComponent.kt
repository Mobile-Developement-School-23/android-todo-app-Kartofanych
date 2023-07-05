package com.example.todoapp.ui.view

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.ui.view.list_adapter.DealsAdapter
import com.example.todoapp.ui.view.list_adapter.OnItemListener
import com.example.todoapp.ui.view.list_adapter.SwipeCallbackInterface
import com.example.todoapp.ui.view.list_adapter.SwipeHelper
import com.example.todoapp.utils.UiState
import com.example.todoapp.utils.internet_connection.ConnectivityObserver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TasksFragmentViewComponent(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentTasksBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
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
                val action = TasksFragmentDirections.actionManageTask(null)
                navController.navigate(action)
            }

            recycler.adapter = DealsAdapter(object : OnItemListener {
                override fun onItemClick(id: String) {
                    val action = TasksFragmentDirections.actionManageTask(id = id)
                    navController.navigate(action)
                }

                override fun onCheckClick(todoItem: TodoItem) {
                    if (internetState == ConnectivityObserver.Status.Available) {
                        viewModel.updateNetworkItem(
                            todoItem.copy(
                                done = !todoItem.done
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "No internet connection, will upload with later. Continue offline.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    viewModel.setTask(
                        todoItem.copy(
                            done = !todoItem.done
                        )
                    )
                }
            })
            val helper = SwipeHelper(object : SwipeCallbackInterface {
                override fun onDelete(todoItem: TodoItem) {
                    if (internetState == ConnectivityObserver.Status.Available) {
                        viewModel.deleteNetworkItem(todoItem.id)
                    } else {
                        Toast.makeText(
                            context,
                            "No internet connection, will upload later. Continue offline.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    viewModel.deleteItem(todoItem)
                }

                override fun onChangeDone(todoItem: TodoItem) {
                    if (internetState == ConnectivityObserver.Status.Available) {
                        viewModel.updateNetworkItem(
                            todoItem.copy(
                                done = !todoItem.done
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "No internet connection, will upload later. Continue offline.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    viewModel.setTask(
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
                if (internetState == ConnectivityObserver.Status.Available) {
                    viewModel.loadNetworkList()
                } else {
                    Toast.makeText(
                        context,
                        "No internet connection, retry later(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                refresher.isRefreshing = false
            }

            floatingLogOut.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(
                    ContextThemeWrapper(
                        context,
                        R.style.AlertDialogCustom
                    )
                )
                builder.apply {
                    val title = if (internetState == ConnectivityObserver.Status.Available) {
                        "Вы уверены, что хотите выйти?"
                    } else {
                        "Вы уверены, что хотите выйти? Возможна потеря данных оффлайн режима."
                    }
                    setMessage(title)
                    setPositiveButton(
                        "Выйти"
                    ) { _, _ ->
                        val action = TasksFragmentDirections.logOut()
                        navController.navigate(action)
                    }
                }
                builder.show()
                    .create()
            }
        }
    }

    private fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.status.collectLatest {
                updateStatusUI(it)
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            viewModel.visibility.collectLatest { visibilityState ->
                updateStateUI(visibilityState)
            }
        }
        lifecycleOwner.lifecycleScope.launch {
            viewModel.countComplete.collectLatest {
                updateCounter(it)
            }
        }

        internetState = viewModel.status.value
    }

    private suspend fun updateStateUI(visibilityState: Boolean) {
        viewModel.data.collect { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (visibilityState) {
                        adapter.submitList(uiState.data)
                    } else {
                        adapter.submitList(uiState.data.filter { !it.done })
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