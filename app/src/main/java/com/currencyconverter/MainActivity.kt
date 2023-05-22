package com.currencyconverter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.currencyconverter.databinding.ActivityMainBinding
import com.currencyconverter.presentation.MainViewModel
import com.currencyconverter.util.CurrencyEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrencyFromSpinner()

        binding.mainBtnConvert.setOnClickListener {
            viewModel.getConversion(
                binding.mainEtAmount.text.toString(),
                binding.mainSpinnerFromCurrency.selectedItem.toString(),
                binding.mainSpinnerToCurrency.selectedItem.toString(),
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is CurrencyEvent.Success -> {
                        showLoadingAnimation(false)
                        enableConversionButton(true)
                        binding.mainTvResult.setTextColor(Color.BLACK)
                        binding.mainTvResult.text = event.resultText.asString(this@MainActivity)
                    }
                    is CurrencyEvent.Failure -> {
                        showLoadingAnimation(false)
                        enableConversionButton(true)
                        binding.mainTvResult.setTextColor(Color.RED)
                        binding.mainTvResult.text = event.errorText.asString(this@MainActivity)
                    }
                    is CurrencyEvent.Loading -> {
                        showLoadingAnimation(true)
                        enableConversionButton(false)
                    }
                    else -> Unit
                }
            }
        }
    }



    private fun showLoadingAnimation(show: Boolean) {
        binding.mainLoadingAnimation.isVisible = show
    }

    private fun enableConversionButton(enable: Boolean) {
        binding.mainBtnConvert.isEnabled = enable
    }


    private fun setCurrencyFromSpinner(){
        binding.mainSpinnerFromCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val itemSelected = parent.getItemAtPosition(position).toString()
                binding.mainTvFrom.text = itemSelected

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.mainSpinnerToCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val itemSelected = parent.getItemAtPosition(position).toString()
                binding.mainTvTo.text = itemSelected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }



    }



}