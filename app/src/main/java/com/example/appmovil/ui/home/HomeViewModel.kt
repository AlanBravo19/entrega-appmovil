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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val baseUrl =
        "https://raw.githubusercontent.com/AlanBravo19/entrega-appmovil/main/productos.json"

    init {
        loadProducts()
    }

    fun refreshProducts() {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true

                //  evitar cache SIEMPRE
                val finalUrl = "$baseUrl?timestamp=${System.nanoTime()}"

                val url = URL(finalUrl)
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "GET"
                conn.useCaches = false
                conn.defaultUseCaches = false

                conn.setRequestProperty("Cache-Control", "no-store, no-cache, must-revalidate")
                conn.setRequestProperty("Pragma", "no-cache")
                conn.setRequestProperty("Expires", "0")

                val json = conn.inputStream.bufferedReader().readText()

                val list = Gson().fromJson(json, Array<Product>::class.java).toList()
                _products.value = list

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
