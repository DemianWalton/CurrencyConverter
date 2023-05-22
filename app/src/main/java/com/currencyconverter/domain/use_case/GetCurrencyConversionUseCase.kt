package com.currencyconverter.domain.use_case

import com.currencyconverter.R
import com.currencyconverter.domain.repository.MainRepository
import com.currencyconverter.util.CurrencyEvent
import com.currencyconverter.util.DataState
import com.currencyconverter.util.UiText
import com.currencyconverter.util.getRateForCurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.math.round

class GetCurrencyConversionUseCase @Inject constructor(
    private val repository: MainRepository,
) {
    suspend operator fun invoke(
        floatAmount: Float,
        fromCurrency: String,
        toCurrency: String
    ): Flow<CurrencyEvent> = flow {
        emit(CurrencyEvent.Loading)

        when (val ratesResponse = repository.getRates(fromCurrency)) {
            is DataState.Error -> emit(CurrencyEvent.Failure(ratesResponse.message!!))
            is DataState.Success -> {
                val rates = ratesResponse.data!!.rates
                val rate = getRateForCurrency(toCurrency, rates)
                if (rate == null) {
                    emit(CurrencyEvent.Failure(UiText.StringResource(R.string.error_api_call_problem)))
                } else {
                    val convertedCurrency = round(floatAmount * rate * 100) / 100
                    emit(
                        CurrencyEvent.Success(
                            UiText.DynamicString(
                                "$floatAmount $fromCurrency = $convertedCurrency $toCurrency"
                            )
                        )
                    )
                }
            }
        }
    }
}


