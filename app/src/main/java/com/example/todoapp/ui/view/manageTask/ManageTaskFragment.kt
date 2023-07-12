package com.example.todoapp.ui.view.manageTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.App
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.ui.stateholders.ManageTaskViewModel


class ManageTaskFragment : Fragment() {

    private val model: ManageTaskViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }


    private lateinit var binding: FragmentNewTaskBinding

    private val args: ManageTaskFragmentArgs by navArgs()

    private var fragmentViewController: ManageTaskFragmentViewController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskBinding.inflate(LayoutInflater.from(context))
        fragmentViewController = ManageTaskFragmentViewController(
            requireContext(),
            findNavController(),
            binding,
            viewLifecycleOwner,
            model,
            args
        ).apply {
            onCreate()
        }

        return binding.root
    }




}
