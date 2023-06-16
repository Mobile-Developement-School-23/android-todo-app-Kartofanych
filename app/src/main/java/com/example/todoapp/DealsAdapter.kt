package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.Importance
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.databinding.ElementListBinding
import com.example.todoapp.utils.DiffUtilCallbackImpl
import java.util.logging.Handler

class DealsAdapter(
    val onItemListener: OnItemListener
) : RecyclerView.Adapter<DealsAdapter.ViewHolder>() {

    private var list = listOf<TodoItem>()

    fun setData(newData:List<TodoItem>){
        val callback = DiffUtilCallbackImpl(
            oldItems = list,
            newItems = newData
        )
        list = newData
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }


    interface OnItemListener{
        fun onItemClick(id: String)

        fun onCheckClick(todoItem: TodoItem)
    }



    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val binding = ElementListBinding.bind(itemView)

        fun bind(item: TodoItem){
            if(item.done) {
                binding.text.text = item.text
                binding.text.paintFlags = binding.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.text.setTextColor(itemView.context.getColor(R.color.tertiary))

                binding.checkBox.isChecked = true
                binding.checkBox.buttonTintList = AppCompatResources.getColorStateList(itemView.context,
                    R.color.green
                )

                binding.importance.visibility = View.GONE
                binding.deadline.visibility = View.GONE



            }else{
                binding.text.text = item.text
                binding.text.paintFlags = binding.text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.text.setTextColor(itemView.context.getColor(R.color.primary))
                binding.checkBox.isChecked = false
                if(item.deadline != null) {
                    binding.deadline.visibility = View.VISIBLE
                    binding.deadline.text = item.deadlineToString()
                }else{
                    binding.deadline.visibility = View.GONE
                }
                when (item.importance) {
                    Importance.URGENT -> {
                        binding.importance.visibility = View.VISIBLE
                        binding.checkBox.buttonTintList =
                            AppCompatResources.getColorStateList(itemView.context, R.color.red)
                        binding.importance.setImageDrawable(AppCompatResources.getDrawable(itemView.context,
                            R.drawable.ic_urgent
                        ))
                    }
                    Importance.LOW -> {
                        binding.importance.visibility = View.VISIBLE
                        binding.checkBox.buttonTintList =
                            AppCompatResources.getColorStateList(itemView.context,
                                R.color.separator
                            )
                        binding.importance.setImageDrawable(AppCompatResources.getDrawable(itemView.context,
                            R.drawable.ic_low
                        ))
                    }
                    Importance.REGULAR -> {
                        binding.importance.visibility = View.GONE
                        binding.checkBox.buttonTintList =
                            AppCompatResources.getColorStateList(itemView.context,
                                R.color.separator
                            )
                    }
                }
            }

            binding.checkBox.setOnClickListener {
                item.done = binding.checkBox.isChecked
                onItemListener.onCheckClick(item)
                notifyItemChanged(absoluteAdapterPosition)
            }


            itemView.setOnClickListener {
                onItemListener.onItemClick(item.id)

            }
        }

    }

    fun getElement(position: Int): TodoItem {
        return list[position]
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.element_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
}