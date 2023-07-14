package com.example.todoapp.utils

import com.example.todoapp.domain.model.Importance
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.sql.Date

fun Date.asLocalDate(): LocalDate =
    Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()

fun Date.asLocalTime(): LocalTime =
    Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalTime()

fun LocalDate.asDate(): Date = Date(atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())

fun LocalTime.asDate(localDate: LocalDate): Date =
    Date(atDate(localDate).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())

fun Importance.toName() : String = when(this){
    Importance.LOW -> "Низкая"
    Importance.REGULAR -> "Средняя"
    Importance.URGENT -> "!!Высокая"
}