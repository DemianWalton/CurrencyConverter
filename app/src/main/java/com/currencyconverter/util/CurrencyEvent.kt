package com.currencyconverter.util

sealed class CurrencyEvent {
    class Success(val resultText: UiText) : CurrencyEvent()
    class Failure(val errorText: UiText) : CurrencyEvent()
    object Loading : CurrencyEvent()
    object Empty : CurrencyEvent()
}