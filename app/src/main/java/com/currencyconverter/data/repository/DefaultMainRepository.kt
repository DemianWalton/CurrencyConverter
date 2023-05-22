package com.currencyconverter.data.repository

import com.currencyconverter.data.CurrencyApi
import com.currencyconverter.data.models.CurrencyResponse
import com.currencyconverter.domain.repository.MainRepository
import com.currencyconverter.util.DataState
import com.currencyconverter.util.UiText
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {

    override suspend fun getRates(base: String): DataState<CurrencyResponse> {
        val response = api.getRates(base)
        val result = response.body()
        return if (response.isSuccessful && result != null) {
            DataState.Success(result)
        } else {
            DataState.Error(UiText.DynamicString(response.message()))
        }
    }
}