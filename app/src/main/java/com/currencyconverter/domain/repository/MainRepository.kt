package com.currencyconverter.domain.repository

import com.currencyconverter.data.models.CurrencyResponse
import com.currencyconverter.util.DataState

interface MainRepository {

    suspend fun getRates(base: String): DataState<CurrencyResponse>
}