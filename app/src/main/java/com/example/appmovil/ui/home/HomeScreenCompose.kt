package com.example.appmovil.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmovil.ui.ui.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompose(
    viewModel: HomeViewModel,
    onLogout: () -> Unit,
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onUserClick: () -> Unit
) {

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Productos") },

                navigationIcon = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ArrowBack, "Salir")
                    }
                },

                actions = {
                    IconButton(onClick = onUserClick) {
                        Icon(Icons.Default.Person, "Usuario")
                    }
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, "Carrito")
                    }
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {

            // 🔵 Botón Historial
            Button(
                onClick = onHistoryClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Historial de Compras")
            }

            // 🔄 Botón Actualizar Catálogo
            Button(
                onClick = { viewModel.refreshProducts() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Actualizar catálogo")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ⏳ Mostrar loading durante la descarga
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                }
            } else {
                // 🟢 Lista de productos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    items(products) { product ->
                        ProductCardCompose(
                            product = product,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}
