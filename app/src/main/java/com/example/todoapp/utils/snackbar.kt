package com.example.todoapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(
    message: String, duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, message, duration).show()
}
