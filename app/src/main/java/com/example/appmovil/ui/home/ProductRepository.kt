package com.example.appmovil.ui.home



import com.example.appmovil.ui.ui.domain.model.Product
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ProductRepository {

    private val jsonUrl = "https://pastebin.com/raw/xANwEfGQ"

    suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val json = URL(jsonUrl).readText()
                Gson().fromJson(json, Array<Product>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
