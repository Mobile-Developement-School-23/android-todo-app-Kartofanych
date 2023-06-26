package com.example.todoapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ElementListBinding
import com.example.todoapp.room.Importance
import com.example.todoapp.room.TodoItem

class ViewHolder(private val binding: ElementListBinding): RecyclerView.ViewHolder(binding.root){

    var todoItem: TodoItem? = null
    fun bind(item: TodoItem, onItemListener: OnItemListener){
        this.todoItem = item
        if(item.done) {
            binding.text.text = item.text
            binding.text.paintFlags = binding.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.text.setTextColor(itemView.context.getColor(R.color.tertiary))

            binding.checkBox.isChecked = true
            binding.checkBox.buttonTintList = AppCompatResources.getColorStateList(
                itemView.context,
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
                    binding.importance.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context,
                            R.drawable.ic_urgent
                        )
                    )
                }
                Importance.LOW -> {
                    binding.importance.visibility = View.VISIBLE
                    binding.checkBox.buttonTintList =
                        AppCompatResources.getColorStateList(
                            itemView.context,
                            R.color.separator
                        )
                    binding.importance.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context,
                            R.drawable.ic_low
                        )
                    )
                }
                Importance.REGULAR -> {
                    binding.importance.visibility = View.GONE
                    binding.checkBox.buttonTintList =
                        AppCompatResources.getColorStateList(
                            itemView.context,
                            R.color.separator
                        )
                }
            }
        }

        binding.checkBox.setOnClickListener {
            onItemListener.onCheckClick(item.id, binding.checkBox.isChecked)
        }


        itemView.setOnClickListener {
            onItemListener.onItemClick(item.id)

        }
    }
    companion object {
        fun create(parent: ViewGroup) = ElementListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let(::ViewHolder)
    }

}