package com.example.appmovil.ui.detail


import androidx.lifecycle.ViewModel
import com.example.appmovil.ui.ui.domain.model.Product
import com.example.appmovil.ui.ui.ui.cart.CartPrefs
import android.content.Context

class ProductDetailViewModel : ViewModel() {

    fun addToCart(context: Context, product: Product, userId: String) {
        CartPrefs.addProduct(context, product, 1, userId)
    }
}
