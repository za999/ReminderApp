package com.example.reminderapp.util

sealed class ResultOf<out T> {
    data class Success<out T>(val value: T): ResultOf<T>()

    data class Error(val throwable: Throwable): ResultOf<Nothing>()
}
