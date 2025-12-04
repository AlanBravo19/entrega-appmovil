package com.example.appmovil.ui.cart


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmovil.ui.ui.ui.cart.CartItem

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(item.product.name)
            Text("$${item.product.price}")
        }

        Row {
            Button(onClick = onDecrease) {
                Text("-")
            }

            Text(
                text = item.quantity.toString(),
                modifier = Modifier.padding(8.dp)
            )

            Button(onClick = onIncrease) {
                Text("+")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onDelete) {
                Text("X")
            }
        }
    }
}
