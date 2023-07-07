package com.example.todoapp.ui.view

import android.app.DatePickerDialog
import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UiState
import com.example.todoapp.ui.stateholders.ManageTaskViewModel
import com.example.todoapp.utils.Constants
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar
import java.util.UUID

class ManageTaskFragmentViewController(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentNewTaskBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val model: ManageTaskViewModel,
    private val args: ManageTaskFragmentArgs
) {

    private lateinit var popupMenu: PopupMenu
    private lateinit var timePickerDialog: DatePickerDialog


    fun onCreate() {
        val id = args.id
        if (id != null) {
            model.getItem(id)
        } else {
            model.setItem()
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.todoItem.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            updateViewsInfo(state.data)
                            setUpViews(state.data)
                            createPopupMenu(state.data)
                        }
                        else -> { /*wtf?*/ }
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
                        context,
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
                    context,
                    R.color.red
                )
            )
            TextViewCompat.setCompoundDrawableTintList(
                binding.delete, AppCompatResources.getColorStateList(
                    context,
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
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
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
                            context,
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
                            context,
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
                            context,
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
        setUpDatePicker(todoItem)

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
                navController.popBackStack()
            }
        }
        binding.editTodo.doAfterTextChanged {
            todoItem.text = binding.editTodo.text.toString()
        }
        binding.close.setOnClickListener {
            YoYo.with(Techniques.BounceIn)
                .duration(Constants.ANIMATION_DURATION)
                .playOn(binding.close)
            navController.popBackStack()
        }

        binding.save.setOnClickListener {
            if (args.id == null) {
                saveNewTask(todoItem)
            } else {
                updateTask(todoItem)
            }
        }
    }

    private fun setUpDatePicker(todoItem: TodoItem) {
        val myCalendar = Calendar.getInstance()
        if (todoItem.deadline != null) {
            myCalendar.time = todoItem.deadline!!
        }
        timePickerDialog = DatePickerDialog(
            context,
            R.style.DatePickerStyle,
            { _, year, month, day ->
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
    }


    private fun saveNewTask(todoItem: TodoItem) {
        todoItem.id = UUID.randomUUID().toString()
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateCreation = Date(System.currentTimeMillis())
        if (todoItem.text.isEmpty()) {
            Toast.makeText(context, "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        model.addItem(todoItem)
        navController.popBackStack()

    }

    private fun updateTask(todoItem: TodoItem) {
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateChanged = Date(System.currentTimeMillis())
        if (todoItem.text.isEmpty()) {
            Toast.makeText(context, "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        model.updateItem(todoItem)
        navController.popBackStack()
    }

    private fun openDatePicker() {
        binding.switchCompat.isChecked = true
        timePickerDialog.show()
    }

}
