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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Productos") },

                navigationIcon = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(Icons.Default.ArrowBack, "Salir")
                    }
                },

                actions = {
                    IconButton(onClick = { onUserClick() }) {
                        Icon(Icons.Default.Person, "Usuario")
                    }
                    IconButton(onClick = { onCartClick() }) {
                        Icon(Icons.Default.ShoppingCart, "Carrito")
                    }
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {

            Button(
                onClick = { onHistoryClick() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Historial de Compras")
            }

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
