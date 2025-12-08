package com.example.appmovil.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class OrderHistoryItem(
    val orderId: String,
    val products: List<String>,
    val total: String,
    val date: String
)

open class SessionManager(private val context: Context?) {

    private val prefs = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()


    // USUARIO


    fun saveUser(
        name: String,
        email: String,
        password: String,
        address: String = "",
        phone: String = ""
    ) {
        prefs?.edit()
            ?.putString("user_name", name)
            ?.putString("user_email", email)
            ?.putString("user_password", password)
            ?.putString("user_address", address)
            ?.putString("user_phone", phone)
            ?.putBoolean("is_logged_in", true)
            ?.apply()
    }

    fun updateUser(name: String, address: String, phone: String) {
        prefs?.edit()
            ?.putString("user_name", name)
            ?.putString("user_address", address)
            ?.putString("user_phone", phone)
            ?.apply()
    }

    open fun getName() = prefs?.getString("user_name", "") ?: ""
    open fun getEmail() = prefs?.getString("user_email", "") ?: ""
    open fun getPassword() = prefs?.getString("user_password", "") ?: ""
    open fun getAddress() = prefs?.getString("user_address", "") ?: ""
    open fun getPhone() = prefs?.getString("user_phone", "") ?: ""

    fun isLoggedIn() = prefs?.getBoolean("is_logged_in", false) ?: false

    //  NO borra cuenta, solo cierra sesión
    fun logout() {
        prefs?.edit()
            ?.putBoolean("is_logged_in", false)
            ?.apply()
    }


    // HISTORIAL POR USUARIO


    private fun getOrdersKey(): String {
        val email = getEmail()
        return "orders_$email" //  Clave única por usuario
    }

    fun saveOrder(order: OrderHistoryItem) {
        val key = getOrdersKey()
        val currentOrders = getOrders().toMutableList()
        currentOrders.add(order)

        val json = gson.toJson(currentOrders)

        prefs?.edit()?.putString(key, json)?.apply()
    }

    fun getOrders(): List<OrderHistoryItem> {
        val key = getOrdersKey()
        val json = prefs?.getString(key, null)

        return if (json != null) {
            val type = object : TypeToken<List<OrderHistoryItem>>() {}.type
            gson.fromJson(json, type)
        } else emptyList()
    }
}
