package com.example.todoapp.utils

enum class Filter(val value: Int) {
    PRIORITY(1),
    DEADLINE(2),
    DATE_CREATION(3);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}