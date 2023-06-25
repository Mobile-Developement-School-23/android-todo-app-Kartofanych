package com.example.todoapp.ui

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.todoapp.DealsAdapter
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.room.TodoItem
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TasksFragment : Fragment() {

    private lateinit var adapter: DealsAdapter

    private val model: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentTasksBinding

    private var modeAll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTasksBinding.inflate(layoutInflater)

        if (savedInstanceState != null) {
            modeAll = savedInstanceState.getBoolean("mode")
            when (modeAll) {
                true -> {
                    binding.visible.setImageResource(R.drawable.ic_invisible)
                }

                false -> {
                    binding.visible.setImageResource(R.drawable.ic_visible)
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = binding.root

        setUpViewModel()
        setUpViews()


        return root
    }


    private fun setUpViewModel() {
        model.changeDone(modeAll)
        lifecycleScope.launch {
            model.data.collectLatest {
                updateRecycler(it)
            }

        }
        lifecycleScope.launch {
            model.countComplete.collectLatest {
                updateNumberDone(it)
            }
        }
        /*model.numberDone.observe(viewLifecycleOwner, Observer {
            updateNumberDone(it)
        })*/
    }

    private fun updateNumberDone(it: Int) {
        binding.numberDone.text = "Выполнено - $it"
    }

    private fun setUpViews() {
        //setUpVisibleButton
        binding.visible.setOnClickListener {
            if (modeAll) {
                modeAll = false
                YoYo.with(Techniques.BounceIn).duration(200).playOn(binding.visible)
                binding.visible.setImageResource(R.drawable.ic_visible)
            } else {
                modeAll = true
                YoYo.with(Techniques.BounceIn).duration(200).playOn(binding.visible)
                binding.visible.setImageResource(R.drawable.ic_invisible)
            }
            model.changeDone(modeAll)
            binding.recycler.scrollToPosition(0)

        }

        //setUp recycler
        adapter = DealsAdapter(onItemListener = object : DealsAdapter.OnItemListener {
            override fun onItemClick(id: String) {
                val action = TasksFragmentDirections.actionManageTask(id = id)
                findNavController().navigate(action)
            }

            override fun onCheckClick(todoItem: TodoItem) {
                model.changeItemDone(todoItem.id, todoItem.done)
            }

        })
        binding.recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)


        //setUpFloatingButton
        binding.floatingNewTask.setOnClickListener {
            val action = TasksFragmentDirections.actionManageTask(id = null)
            findNavController().navigate(action)
        }

        /*binding.floatingSettings.setOnClickListener {
            val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(
                ContextThemeWrapper(
                    context,
                    R.style.AlertDialogCustom
                )
            )
            builder.apply {

                setSingleChoiceItems(arrayOf<String>(
                    "По приоритету",
                    "По дате дедлайна",
                    "По дате создания"
                ),
                    filter.value - 1,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        filter = Filter.fromInt(i + 1)
                        model.getData(modeAll, filter)
                        dialogInterface.dismiss()
                    })
                setTitle("Фильтровать:")


            }
            builder.show()
        }*/


    }


    private fun updateRecycler(items: List<TodoItem>) {
        adapter.setData(items)
    }


    private val touchCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.absoluteAdapterPosition
            val item = adapter.getElement(position)

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    model.deleteItem(item)
                }

                ItemTouchHelper.RIGHT -> {
                    model.changeItemDone(item.id, !item.done)
                    adapter.notifyItemChanged(position)
                }
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            RecyclerViewSwipeDecorator
                .Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.back_secondary
                    )
                )
                .addSwipeRightBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                .addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                .addSwipeRightActionIcon(R.drawable.ic_check_24dp)
                .addSwipeLeftActionIcon(R.drawable.ic_delete_24dp)
                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mode", modeAll)
    }


}