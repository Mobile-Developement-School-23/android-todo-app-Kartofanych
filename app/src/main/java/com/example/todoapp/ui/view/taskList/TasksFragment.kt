package com.example.todoapp.ui.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.App
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.ui.stateholders.MainViewModel

class TasksFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }

    private var fragmentViewController: TasksFragmentViewController? = null

    private lateinit var binding:FragmentTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTasksBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = binding.root
        fragmentViewController = TasksFragmentViewController(
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
        fragmentViewController = null
    }

}
