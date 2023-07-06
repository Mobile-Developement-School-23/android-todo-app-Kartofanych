package com.example.todoapp.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UiState
import com.example.todoapp.ui.stateholders.ManageTaskViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar
import java.util.UUID


class ManageTaskFragment : Fragment() {

    private val model: ManageTaskViewModel by viewModels { (requireContext().applicationContext as App).appComponent.viewModelsFactory() }


    private lateinit var popupMenu: PopupMenu
    private lateinit var timePickerDialog: DatePickerDialog


    private lateinit var binding: FragmentNewTaskBinding

    private val args: ManageTaskFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentNewTaskBinding.inflate(LayoutInflater.from(context)).also { binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext().applicationContext as App).appComponent.inject(this)

        val id = args.id
        if (id != null) {
            model.getItem(id)

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.todoItem.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                updateViewsInfo(state.data)
                                setUpViews(state.data)
                                createPopupMenu(state.data)
                            }

                            else -> {}
                        }
                    }
                }
            }
        }else{
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    model.todoItem.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                updateViewsInfo(state.data)
                                setUpViews(state.data)
                                createPopupMenu(state.data)
                            }

                            else -> {
                                setUpViews(TodoItem())
                                createPopupMenu(TodoItem())
                            }
                        }
                    }
                }
            }
        }

    }


    private fun updateViewsInfo(todoItem: TodoItem) {
        binding.editTodo.setText(todoItem.text)

        when (todoItem.importance) {
            Importance.LOW -> {
                binding.importanceText.text = "Низкая"
            }

            Importance.URGENT -> {
                binding.importanceText.text = "!! Высокая"
                binding.importanceText.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.red
                    )
                )
            }

            Importance.REGULAR -> {
                binding.importanceText.text = "Нет"
            }
        }

        if (todoItem.deadline != null) {
            binding.date.visibility = View.VISIBLE
            binding.date.text = todoItem.deadlineToString()
            binding.switchCompat.isChecked = true
        }


        if (todoItem.id != "-1") {
            binding.delete.setTextColor(
                AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.red
                )
            )
            TextViewCompat.setCompoundDrawableTintList(
                binding.delete, AppCompatResources.getColorStateList(
                    requireContext(),
                    R.color.red
                )
            )
        }

    }

    private fun createPopupMenu(todoItem: TodoItem) {
        popupMenu = PopupMenu(context, binding.importanceText)
        popupMenu.menuInflater.inflate(R.menu.popup_importance_menu, popupMenu.menu)

        val highElement: MenuItem = popupMenu.menu.getItem(2)
        val s = SpannableString("!! Высокая")
        s.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)),
            0,
            s.length,
            0
        )
        highElement.title = s

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_middle -> {
                    binding.importanceText.text = "Нет"
                    binding.importanceText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.tertiary
                        )
                    )
                    todoItem.importance = Importance.REGULAR
                    return@setOnMenuItemClickListener true
                }

                R.id.item_low -> {
                    binding.importanceText.text = "Низкая"
                    binding.importanceText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.tertiary
                        )
                    )
                    todoItem.importance = Importance.LOW
                    return@setOnMenuItemClickListener true
                }

                R.id.item_high -> {
                    binding.importanceText.text = "!! Высокая"
                    binding.importanceText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    todoItem.importance = Importance.URGENT
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
    }


    private fun setUpViews(todoItem: TodoItem) {

        val myCalendar = Calendar.getInstance()
        if (todoItem.deadline != null) {
            myCalendar.time = todoItem.deadline!!
        }

        timePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerStyle,
            { view, year, month, day ->
                binding.date.visibility = View.VISIBLE
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                todoItem.deadline = Date(myCalendar.timeInMillis)
                binding.date.text = todoItem.deadlineToString()
            },
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        timePickerDialog.setOnCancelListener {
            if (binding.date.visibility == View.INVISIBLE) {
                binding.switchCompat.isChecked = false
            }
        }


        binding.switchCompat.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                openDatePicker()
            } else {
                binding.date.visibility = View.INVISIBLE
                todoItem.deadline = null
            }
        }

        binding.chooseImportance.setOnClickListener {
            popupMenu.show()
        }

        binding.chooseDate.setOnClickListener {
            openDatePicker()
        }


        binding.delete.setOnClickListener {
            if (args.id != null) {
                model.deleteItem(todoItem)
                findNavController().popBackStack()

            }
        }

        binding.editTodo.doAfterTextChanged {
            todoItem.text = binding.editTodo.text.toString()
        }

        binding.close.setOnClickListener {
            YoYo.with(Techniques.BounceIn)
                .duration(200)
                .playOn(binding.close)

            findNavController().popBackStack()
        }


        binding.save.setOnClickListener {
            if (args.id == null) {
                saveNewTask(todoItem)
            } else {
                updateTask(todoItem)
            }

        }
    }


    private fun saveNewTask(todoItem: TodoItem) {
        todoItem.id = UUID.randomUUID().toString()
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateCreation = Date(System.currentTimeMillis())
        if (todoItem.text.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        model.addItem(todoItem)
        findNavController().popBackStack()

    }

    private fun updateTask(todoItem: TodoItem) {
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateChanged = Date(System.currentTimeMillis())
        if (todoItem.text.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }


        model.updateItem(todoItem)
        findNavController().popBackStack()
    }

    private fun openDatePicker() {
        binding.switchCompat.isChecked = true
        timePickerDialog.show()
    }



}