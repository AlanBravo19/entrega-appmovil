package com.example.appmovil.data

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(
        name: String,
        email: String,
        password: String,
        address: String = "",
        phone: String = ""
    ) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_password", password)
            .putString("user_address", address)
            .putString("user_phone", phone)
            .putBoolean("is_logged_in", true)
            .apply()
    }

    fun updateUser(name: String, address: String, phone: String) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_address", address)
            .putString("user_phone", phone)
            .apply()
    }


    fun getName() = prefs.getString("user_name", "")
    fun getEmail() = prefs.getString("user_email", "")
    fun getPassword() = prefs.getString("user_password", "")
    fun getAddress() = prefs.getString("user_address", "")
    fun getPhone() = prefs.getString("user_phone", "")

    fun isLoggedIn() = prefs.getBoolean("is_logged_in", false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}