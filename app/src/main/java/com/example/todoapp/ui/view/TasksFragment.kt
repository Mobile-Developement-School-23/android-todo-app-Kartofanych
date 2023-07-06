package com.example.todoapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.App
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.utils.getAppComponent

class TasksFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels { (requireContext().applicationContext as App).appComponent.viewModelsFactory() }


    private var fragmentViewComponent: TasksFragmentViewComponent? = null

    private lateinit var binding:FragmentTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTasksBinding.inflate(LayoutInflater.from(context))
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = binding.root
        fragmentViewComponent = TasksFragmentViewComponent(
            requireContext(),
            findNavController(),
            binding,
            viewLifecycleOwner,
            viewModel
        ).apply {
            setUpViews()
        }

        viewModel.loadData()
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentViewComponent = null
    }

}
