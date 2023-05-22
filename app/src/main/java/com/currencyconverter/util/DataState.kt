package com.currencyconverter.util

sealed class DataState<T>(val data: T?, val message: UiText?) {
    class Success<T>(data: T) : DataState<T>(data, null)
    class Error<T>(message: UiText) : DataState<T>(null, message)
}