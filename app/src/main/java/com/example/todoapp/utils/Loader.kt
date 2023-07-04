package com.example.todoapp.utils

import com.example.todoapp.data.repository.ItemsRepository
import com.example.todoapp.domain.model.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface Loader<T> {

    class State<T>(
        val data: T,
        val isLoading: Boolean,
        val isError: Boolean
    )

    val state: Flow<State<T>>

    fun load()
}

class MyLoader(
    private val scope: CoroutineScope
) : Loader<List<TodoItem>> {

    private val repository:ItemsRepository by localeLazy()

    private val _state = MutableStateFlow<InnerState<List<TodoItem>>>(
        InnerState(
            listOf(),
            false,
            false,
            false,
            false,
            false
        )
    )

    override val state: Flow<Loader.State<List<TodoItem>>> = _state.map {
        Loader.State(
            data = it.currentData,
            isLoading = it.isLoadingFromCache || it.isLoadingFromRemote,
            isError = it.remoteError,
        )
    }

    private fun launchWork(block: (InnerState<List<TodoItem>>) -> InnerState<List<TodoItem>>) {
        scope.launch(Dispatchers.IO) {
            _state.value = block(_state.value)
        }
    }

    override fun load() {
        launchWork { state ->
            launchLoad(state)
        }
    }

    private fun launchLoad(state: InnerState<List<TodoItem>>): InnerState<List<TodoItem>> {
        if (!state.loadedFromCache && !state.isLoadingFromCache) {
            return launchCacheLoad(state = state)
        }
        if (state.loadedFromCache
            && !state.isLoadingFromCache
            && !state.isLoadingFromRemote
            && !state.remoteError
        ) {
            return launchRemoteLoad(state)
        }
        return state
    }

    private fun launchRemoteLoad(state: InnerState<List<TodoItem>>): InnerState<List<TodoItem>> {
        scope.launch(Dispatchers.IO) {
            /*val result = repository.lo

            if (result == null) {
                launchWork {
                    it.copy(
                        isLoadingFromRemote = false,
                        remoteError = true
                    )
                }
                return@launch
            }
            launchWork {
                onRemoteLoaded(result, it)
            }*/
        }
        return state.copy(
            isLoadingFromRemote = true
        )
    }

    private fun onRemoteLoaded(
        chunk: List<TodoItem>,
        state: InnerState<List<TodoItem>>
    ): InnerState<List<TodoItem>> {
        return state.copy(
            currentData = chunk,
            loadedFromRemote = true,
            isLoadingFromRemote = false,
            remoteError = false
        )
    }

    private fun launchCacheLoad(state: InnerState<List<TodoItem>>): InnerState<List<TodoItem>> {
        scope.launch(Dispatchers.IO) {
            val chunk = repository.getAllData()
            /*launchWork {
                onCacheLoaded(chunk, it)
            }*/
        }
        return state.copy(
            isLoadingFromCache = true
        )
    }

    private fun onCacheLoaded(
        chunk: List<TodoItem>,
        state: InnerState<List<TodoItem>>
    ): InnerState<List<TodoItem>> {
        return state.copy(
            currentData = chunk,
            isLoadingFromCache = false,
            loadedFromCache = true
        )
    }
}



data class InnerState<T>(
    val currentData: T,
    val isLoadingFromCache: Boolean,
    val loadedFromCache: Boolean,
    val isLoadingFromRemote: Boolean,
    val loadedFromRemote: Boolean,
    val remoteError: Boolean
)