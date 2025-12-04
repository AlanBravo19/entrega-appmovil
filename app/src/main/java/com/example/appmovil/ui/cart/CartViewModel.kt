package com.example.appmovil.ui.cart



import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmovil.ui.ui.ui.cart.CartItem
import com.example.appmovil.ui.ui.ui.cart.CartPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    fun loadCart(context: Context, userId: String) {
        viewModelScope.launch {
            _items.value = CartPrefs.getCart(context, userId)
        }
    }

    fun updateQuantity(context: Context, userId: String, productId: String, qty: Int) {
        viewModelScope.launch {
            CartPrefs.updateQuantity(context, productId, qty, userId)
            loadCart(context, userId)
        }
    }

    fun removeItem(context: Context, userId: String, productId: String) {
        viewModelScope.launch {
            CartPrefs.removeProduct(context, productId, userId)
            loadCart(context, userId)
        }
    }

    fun getTotal(): Double {
        return _items.value.sumOf { it.product.price * it.quantity }
    }
}
