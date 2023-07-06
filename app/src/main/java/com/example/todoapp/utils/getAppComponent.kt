package com.example.todoapp.utils

import androidx.fragment.app.Fragment
import com.example.todoapp.App
import com.example.todoapp.di.AppComponent

fun Fragment.getAppComponent(): AppComponent =
(requireContext() as App).appComponent