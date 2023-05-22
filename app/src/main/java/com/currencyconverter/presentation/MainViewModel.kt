package com.currencyconverter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyconverter.R
import com.currencyconverter.domain.use_case.GetCurrencyConversionUseCase
import com.currencyconverter.util.CurrencyEvent
import com.currencyconverter.util.DispatcherProvider
import com.currencyconverter.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyConversionUseCase: GetCurrencyConversionUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {


    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun getConversion(amountStr: String, fromCurrency: String, toCurrency: String) {

        val amountInFloat = amountStr.toFloatOrNull()

        if (amountInFloat == null) {
            _conversion.value =
                CurrencyEvent.Failure(UiText.StringResource(R.string.error_amount_invalid))
            return
        }

        viewModelScope.launch(dispatchers.io) {
            currencyConversionUseCase.invoke(
                floatAmount = amountInFloat,
                fromCurrency = fromCurrency,
                toCurrency = toCurrency
            ).catch { e ->
                /**We can map the exception to show a custom error msg..*/
                _conversion.value =
                    CurrencyEvent.Failure(UiText.StringResource(R.string.error_api_call_exception))
            }.collect { res ->
                when (res) {
                    is CurrencyEvent.Failure -> {
                        _conversion.value = CurrencyEvent.Failure(res.errorText)
                    }
                    is CurrencyEvent.Success -> {
                        _conversion.value = CurrencyEvent.Success(res.resultText)
                    }
                    is CurrencyEvent.Loading -> {
                        _conversion.value = CurrencyEvent.Loading
                    }
                    else -> {
                        _conversion.value = CurrencyEvent.Empty
                    }
                }
            }
        }
    }
}