package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.room.Importance
import com.example.todoapp.room.TodoItem
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*


class NewTaskFragment : Fragment() {

    private var todoItem = TodoItem()

    private lateinit var popupMenu: PopupMenu
    private lateinit var timePickerDialog: DatePickerDialog


    private lateinit var binding: FragmentNewTaskBinding

    private val args: NewTaskFragmentArgs by navArgs()

    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewTaskBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val id = args.id
        if (id != null && savedInstanceState == null) {
            model.getItem(id)
            lifecycleScope.launch {
                model.item.collectLatest {
                    todoItem = it.copy()
                    updateViewsInfo()
                    setUpViews()
                }
            }
        }else if(savedInstanceState == null){
            setUpViews()
        }

        if (savedInstanceState != null) {
            val gson = Gson()
            todoItem = gson.fromJson(savedInstanceState.getString("todoItem"), TodoItem::class.java)
            updateViewsInfo()
            setUpViews()
        }




        createPopupMenu()


        return binding.root
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

    private fun createPopupMenu() {
        popupMenu = PopupMenu(context, binding.importanceText)
        popupMenu.menuInflater.inflate(R.menu.popup_importance_menu, popupMenu.menu)

        //example
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


    private fun setUpViews() {

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
                YoYo.with(Techniques.BounceIn)
                    .duration(200)
                    .playOn(binding.delete)
                model.deleteItem(todoItem.id)

                findNavController().popBackStack()

            }
        }

        binding.close.setOnClickListener {
            YoYo.with(Techniques.BounceIn)
                .duration(200)
                .playOn(binding.close)

            findNavController().popBackStack()
        }


        binding.save.setOnClickListener {
            YoYo.with(Techniques.BounceIn)
                .duration(200)
                .playOn(binding.save)
            if (args.id == null) {
                saveNewTask()
            } else {
                updateTask()
            }

        }
    }


    private fun saveNewTask() {
        todoItem.id = model.getLastId()
        todoItem.text = binding.editTodo.text.toString()
        todoItem.dateCreation = Date(System.currentTimeMillis())
        //
        if (todoItem.text.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните что нужно сделать!", Toast.LENGTH_SHORT)
                .show()
            return
        }


        model.addItem(todoItem)
        findNavController().popBackStack()

    }

    private fun updateTask() {
        todoItem.text = binding.editTodo.text.toString()
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

    private fun saveStates() {
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

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveStates()
        outState.putString("todoItem", todoItem.toString())
    }


}