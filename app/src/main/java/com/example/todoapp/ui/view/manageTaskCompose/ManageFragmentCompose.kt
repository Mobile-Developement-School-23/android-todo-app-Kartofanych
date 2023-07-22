package com.example.todoapp.ui.view.manageTaskCompose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.App
import com.example.todoapp.domain.model.Mode
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.ui.stateholders.ManageTaskComposeViewModel
import com.example.todoapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageFragmentCompose : Fragment() {

    private val viewModel: ManageTaskComposeViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val args: ManageFragmentComposeArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = args.id
        if(id != null) {
            viewModel.getItem(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        (requireContext().applicationContext as App).appComponent.inject(this)
        return ComposeView(requireContext()).apply {
            setContent {
                YandexTodoTheme(
                    darkTheme = when (sharedPreferencesHelper.getMode()) {
                        Mode.NIGHT -> true
                        Mode.LIGHT -> false
                        Mode.SYSTEM -> isSystemInDarkTheme()
                    }
                ) {
                    ManageTaskScreen(
                        state = viewModel.todoItem.collectAsState(),
                        onEvent = viewModel::handleEvent,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navController = findNavController()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.actions.collect{
                    if(it is FragmentActions.NavigateBack){
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
