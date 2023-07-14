package com.example.todoapp.ui.stateholders

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ui.view.manageTaskCompose.FragmentActions
import com.example.todoapp.ui.view.manageTaskCompose.FragmentUIEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class ManageTaskComposeViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _actions: Channel<FragmentActions> = Channel(Channel.BUFFERED)
    val actions: Flow<FragmentActions> = _actions.receiveAsFlow()

    private val _todoItem: MutableStateFlow<TodoItem> = MutableStateFlow(TodoItem())
    val todoItem = _todoItem.asStateFlow()


    fun getItem(id: String) {
        if (_todoItem.value.id == "-1") {
            coroutineScope.launch(Dispatchers.IO) {
                _todoItem.emit(repository.getItem(id))
            }
        }
    }


    private fun addItem(todoItem: TodoItem) {
        todoItem.id = UUID.randomUUID().toString()
        todoItem.dateCreation.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.addItem(todoItem.copy())
        }
    }

    private fun deleteItem(todoItem: TodoItem) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoItem)
        }
    }

    private fun updateItem(task: TodoItem) {
        task.dateChanged?.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.changeItem(task)
        }
    }

    fun handleEvent(event: FragmentUIEvents) {
        when (event) {
            is FragmentUIEvents.ChangeTitle -> _todoItem.update { _todoItem.value.copy(text = event.text) }
            is FragmentUIEvents.ChangeUrgency -> _todoItem.update { _todoItem.value.copy(importance = event.importance) }
            is FragmentUIEvents.ChangeDeadline -> _todoItem.update { _todoItem.value.copy(deadline = event.deadline) }

            FragmentUIEvents.SaveTodo -> {
                if (_todoItem.value.id == "-1") {
                    addItem(_todoItem.value)
                    _actions.trySend(FragmentActions.NavigateBack)
                } else {
                    updateItem(_todoItem.value)
                    _actions.trySend(FragmentActions.NavigateBack)
                }
            }

            FragmentUIEvents.DeleteTodo -> {
                deleteItem(_todoItem.value)
                _actions.trySend(FragmentActions.NavigateBack)
            }

            FragmentUIEvents.Close -> _actions.trySend(FragmentActions.NavigateBack)
        }
    }


}
