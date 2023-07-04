package com.example.todoapp.ui.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.utils.internet_connection.ConnectivityObserver
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar
import java.util.UUID

class ManageTasksFragmentViewComponent(
    private val context: Context,
    private val navController: NavController,
    private val binding: FragmentNewTaskBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
    private val id:String?,
    private val savedInstanceState:Bundle?
) {

    private var todoItem = TodoItem()

    private lateinit var popupMenu: PopupMenu
    private lateinit var timePickerDialog: DatePickerDialog


    fun setUpViews(){
        if (id != null && savedInstanceState == null) {
            viewModel.getItem(id)
            lifecycleOwner.lifecycleScope.launch {
                viewModel.item.collect {
                    todoItem = it
                    if (todoItem.id != "-1") {
                        updateViewsInfo()
                        setUpViewsInfo()
                    }
                }
            }
        } else if (savedInstanceState == null) {
            setUpViewsInfo()
        }

        if (savedInstanceState != null) {
            updateViewsInfo()
            setUpViewsInfo()
        }

        createPopupMenu()
    }


    private fun updateViewsInfo() {
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

    private fun createPopupMenu() {
        popupMenu = PopupMenu(context, binding.importanceText)
        popupMenu.menuInflater.inflate(R.menu.popup_importance_menu, popupMenu.menu)

        //example
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


    private fun setUpViewsInfo() {

        val myCalendar = Calendar.getInstance()
        if (todoItem.deadline != null) {
            myCalendar.time = todoItem.deadline!!
        }

        timePickerDialog = DatePickerDialog(
            context,
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
            if (id != null) {
                if (viewModel.status.value == ConnectivityObserver.Status.Available) {
                    viewModel.deleteNetworkItem(todoItem.id)
                } else {
                    Toast.makeText(
                        context,
                        "No network, will delete later. Continue offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.deleteItem(todoItem)
                viewModel.nullItem()
                navController.popBackStack()

            }
        }

        binding.close.setOnClickListener {
            YoYo.with(Techniques.BounceIn)
                .duration(200)
                .playOn(binding.close)

            viewModel.nullItem()
            navController.popBackStack()
        }


        binding.save.setOnClickListener {
            if (id == null) {
                saveNewTask()
            } else {
                updateTask()
            }

        }
    }


    private fun saveNewTask() {
        todoItem.id = UUID.randomUUID().toString()
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateCreation = Date(System.currentTimeMillis())
        if (todoItem.text.isEmpty()) {
            Toast.makeText(context, "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }


        if (viewModel.status.value == ConnectivityObserver.Status.Available) {
            viewModel.uploadNetworkItem(todoItem)
        } else {
            Toast.makeText(
                context,
                "No network, will upload later. Continue offline.",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.addItem(todoItem)
        viewModel.nullItem()
        navController.popBackStack()

    }

    private fun updateTask() {
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateChanged = Date(System.currentTimeMillis())
        if (todoItem.text.isEmpty()) {
            Toast.makeText(context, "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (viewModel.status.value == ConnectivityObserver.Status.Available) {
            viewModel.updateNetworkItem(todoItem)
        } else {
            Toast.makeText(
                context,
                "No network, will update later. Continue offline.",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.setTask(todoItem)
        viewModel.nullItem()
        navController.popBackStack()
    }

    private fun openDatePicker() {
        binding.switchCompat.isChecked = true
        timePickerDialog.show()
    }

    fun saveStates() {
        todoItem.text = binding.editTodo.text.toString()
        when (binding.importanceText.text) {
            "!!Высокая" -> {
                todoItem.importance = Importance.URGENT
            }

            "Нет" -> {
                todoItem.importance = Importance.REGULAR
            }

            "Низкая" -> {
                todoItem.importance = Importance.LOW
            }
        }
        Log.d("1", "saving")
        //viewModel.setItem(todoItem)

    }


}