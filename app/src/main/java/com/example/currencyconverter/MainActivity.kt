package com.example.currencyconverter

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Tỷ giá cố định
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "VND" to 23185.0,
        "EUR" to 0.85,
        "JPY" to 110.0,
        "GBP" to 0.75
    )

    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var amountInput: EditText
    private lateinit var resultOutput: TextView
    private lateinit var exchangeRateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Xử lý Edge-to-Edge cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Liên kết các thành phần giao diện
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner)
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner)
        amountInput = findViewById(R.id.amountInput)
        resultOutput = findViewById(R.id.resultOutput)
        exchangeRateText = findViewById(R.id.exchangeRateText)
        val convertButton: Button = findViewById(R.id.convertButton)

        // Cài đặt Spinner
        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        // Cập nhật tỷ giá khi thay đổi Spinner
        fromCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateExchangeRate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        toCurrencySpinner.onItemSelectedListener = fromCurrencySpinner.onItemSelectedListener

        // Chuyển đổi tiền tệ khi nhấn nút
        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun updateExchangeRate() {
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()

        if (fromCurrency != toCurrency) {
            val rate = exchangeRates[toCurrency]!! / exchangeRates[fromCurrency]!!
            exchangeRateText.text = String.format("Tỷ giá: 1 %s = %.2f %s", fromCurrency, rate, toCurrency)
        } else {
            exchangeRateText.text = "Tỷ giá: N/A"
        }
    }

    private fun convertCurrency() {
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()
        val amount = amountInput.text.toString().toDoubleOrNull()

        if (amount != null && fromCurrency != toCurrency) {
            val result = amount * (exchangeRates[toCurrency]!! / exchangeRates[fromCurrency]!!)
            resultOutput.text = String.format("%.2f %s", result, toCurrency)
        } else {
            resultOutput.text = "Vui lòng nhập số tiền hợp lệ!"
        }
    }
}