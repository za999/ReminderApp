package com.example.reminderapp.util

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

fun Long.getLocalDateFromMillis(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

}
fun Long.getLocalTimeFromMillis(): LocalTime {
    return LocalTime.MIDNIGHT.plus(Duration.ofMillis(this))
}

fun LocalDate.fromLocalDateToMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun LocalDateTime.fromLocalDateTimeToMillis(): Long {
    val zoneId = this.atZone(ZoneId.systemDefault())
    return this.atZone(zoneId.zone).toInstant().toEpochMilli()
}


fun LocalTime.fromLocalTimeToMillis(): Long {
    return Duration.between(LocalTime.MIDNIGHT, this).toMillis()
}


fun Long.getLocalDateTimeFromMillisToString(): String {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate().toString()

}

fun LocalDate.untilEndOfDay(): Long {
    return this
        .plusDays(1)
        .fromLocalDateToMillis()
}

fun LocalDate.beginningOfDay(): Long {
    return this
        .minusDays(1)
        .fromLocalDateToMillis()
}

fun LocalDate.getAlarmTriggerTime(localTime: LocalTime?): Long {
    return if (localTime != null) {
        this.atTime(localTime)
    } else {
        this.atStartOfDay()
    }.fromLocalDateTimeToMillis()
}

