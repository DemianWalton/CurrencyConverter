package com.currencyconverter.util

fun getRateForCurrency(toCurrency: String, rates: Map<String, Double>): Double? =
    rates[toCurrency]