package com.example.appmovil.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmovil.ui.ui.domain.model.Product
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class HomeViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val jsonUrl = "https://pastebin.com/raw/xANwEfGQ"

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(jsonUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val json = conn.inputStream.bufferedReader().readText()

                val list = Gson().fromJson(json, Array<Product>::class.java).toList()

                _products.value = list

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
