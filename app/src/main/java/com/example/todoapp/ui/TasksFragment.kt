package com.example.todoapp.ui

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.todoapp.adapter.DealsAdapter
import com.example.todoapp.adapter.OnItemListener
import com.example.todoapp.R
import com.example.todoapp.adapter.SwipeCallbackInterface
import com.example.todoapp.adapter.SwipeHelper
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.room.TodoItem
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TasksFragment : Fragment() {

        private val viewModel: MainViewModel by activityViewModels()
        private var binding: FragmentTasksBinding? = null
        private val adapter: DealsAdapter? get() = views { recycler.adapter as DealsAdapter }

        private var modeAll:Boolean = false
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = FragmentTasksBinding.inflate(LayoutInflater.from(context)).also { binding = it }.root


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            views {
                if(savedInstanceState!=null){
                    if(savedInstanceState.getBoolean("modeAll")){
                        modeAll = true
                        visible.setImageResource(R.drawable.ic_invisible)
                    }
                }
                if(viewModel.modeAll){
                    modeAll = true
                    visible.setImageResource(R.drawable.ic_invisible)
                }

                floatingNewTask.setOnClickListener {
                    val action = TasksFragmentDirections.actionManageTask(null)
                    findNavController().navigate(action)
                }

                recycler.adapter = DealsAdapter(object : OnItemListener {
                    override fun onItemClick(id: String) {
                        val action = TasksFragmentDirections.actionManageTask(id = id)
                        findNavController().navigate(action)
                    }

                    override fun onCheckClick(id: String, done: Boolean) {
                        viewModel.changeItemDone(id, done)
                    }

                })
                val helper = SwipeHelper(object : SwipeCallbackInterface {
                    override fun onDelete(todoItem: TodoItem) {
                        viewModel.deleteItem(todoItem.id)
                    }

                    override fun onChangeDone(todoItem: TodoItem) {
                        viewModel.changeItemDone(todoItem.id, !todoItem.done)
                    }

                }, requireContext())
                helper.attachToRecyclerView(recycler)


                visible.setOnClickListener {
                    modeAll = !modeAll
                    when (modeAll) {
                        true -> {
                            visible.setImageResource(R.drawable.ic_invisible)
                        }
                        false -> {
                            visible.setImageResource(R.drawable.ic_visible)
                        }
                    }
                    viewModel.changeDone(modeAll)
                }


            }


            viewModel.changeDone(modeAll)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.data.collect {
                    updateUI(it)
                }
            }


        }

        private fun updateUI(list: List<TodoItem>) {
            Log.d("1", list.size.toString())
            adapter?.submitList(list)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putBoolean("modeAll", modeAll)
        }


        private fun <T : Any> views(block: FragmentTasksBinding.() -> T): T? = binding?.block()


    }

