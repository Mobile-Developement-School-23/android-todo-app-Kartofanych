package com.example.todoapp.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class SwipeCallback(
    private val swipeCallback: SwipeCallbackInterface,
    private val context: Context
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            (viewHolder as? ViewHolder)?.todoItem?.let { swipeCallback.onDelete(it) }
        } else if (direction == ItemTouchHelper.RIGHT) {
            (viewHolder as? ViewHolder)?.todoItem?.let { swipeCallback.onChangeDone(it) }
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
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.back_secondary
                )
            )
            .addSwipeRightBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )
            .addSwipeLeftBackgroundColor(
                ContextCompat.getColor(
                    context,
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