package com.example.appmovil.ui.detail

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appmovil.ui.ui.domain.model.Product

@Composable
fun ProductDetailScreenCompose(
    product: Product,
    viewModel: ProductDetailViewModel,
    onBack: () -> Unit,
    currentUserId: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // ATRÁS
        Button(onClick = { onBack() }) {
            Text("◀ Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Precio
        Text(
            text = "$${product.price}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Descripción
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Agregar al carrito
        Button(
            onClick = {
                viewModel.addToCart(context, product, currentUserId)
                Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar al carrito")
        }
    }
}
