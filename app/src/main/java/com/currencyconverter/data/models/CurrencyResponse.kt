package com.currencyconverter.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    @SerialName("base")
    val base: String,
    @SerialName("date")
    val date: String,
    @SerialName("rates")
    val rates: Map<String, Double>,
    @SerialName("success")
    val success: Boolean,
    @SerialName("timestamp")
    val timestamp: Int
)