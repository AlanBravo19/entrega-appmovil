package com.example.appmovil.ui.home


import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.appmovil.ui.ui.domain.model.Product
import com.example.appmovil.ui.ui.domain.model.ProductsData

class HomeViewModel : ViewModel() {

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = ProductsData.productList
    }
}
