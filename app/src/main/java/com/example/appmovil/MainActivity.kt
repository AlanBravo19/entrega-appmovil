package com.example.appmovil

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.google.gson.Gson
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import com.example.appmovil.data.SessionManager

import com.example.appmovil.ui.login.LoginScreenCompose
import com.example.appmovil.ui.login.LoginViewModel
import com.example.appmovil.ui.login.RegisterScreenCompose
import com.example.appmovil.ui.login.RegisterViewModel

import com.example.appmovil.ui.home.HomeScreenCompose
import com.example.appmovil.ui.home.HomeViewModel

import com.example.appmovil.ui.profile.ProfileScreenCompose
import com.example.appmovil.ui.profile.ProfileViewModel

import com.example.appmovil.ui.detail.ProductDetailScreenCompose
import com.example.appmovil.ui.detail.ProductDetailViewModel

import com.example.appmovil.ui.cart.CartScreenCompose
import com.example.appmovil.ui.ui.domain.model.Product
import com.example.appmovil.ui.theme.AppMovilTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AppMovilTheme {

                val navController = rememberNavController()
                val session = SessionManager(this)

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    // LOGIN
                    composable("login") {

                        val vm = LoginViewModel(session)

                        LoginScreenCompose(
                            viewModel = vm,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onGoRegister = { navController.navigate("register") }
                        )
                    }

                    // REGISTRO
                    composable("register") {

                        val vm = RegisterViewModel(session)

                        RegisterScreenCompose(
                            viewModel = vm,
                            onRegisterSuccess = { navController.navigate("login") },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // HOME
                    composable("home") {

                        val homeVM: HomeViewModel = viewModel()

                        HomeScreenCompose(
                            viewModel = homeVM,

                            onLogout = {
                                session.logout()
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },

                            onProductClick = { product ->
                                val json = Gson().toJson(product)
                                val encoded = URLEncoder.encode(
                                    json,
                                    StandardCharsets.UTF_8.toString()
                                )
                                navController.navigate("productDetail/$encoded")
                            },

                            onCartClick = {
                                val userId = session.getEmail() ?: "guest"
                                navController.navigate("cart/$userId")
                            },

                            onHistoryClick = {},

                            onUserClick = {
                                navController.navigate("profile")
                            }
                        )
                    }

                    // PERFIL
                    composable("profile") {

                        val vm = ProfileViewModel(session)

                        ProfileScreenCompose(
                            viewModel = vm,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // DETALLE PRODUCTO
                    composable("productDetail/{json}") { entry ->

                        val encodedJson = entry.arguments?.getString("json") ?: ""
                        val decoded = URLDecoder.decode(
                            encodedJson,
                            StandardCharsets.UTF_8.toString()
                        )

                        val product = Gson().fromJson(decoded, Product::class.java)
                        val vm: ProductDetailViewModel = viewModel()

                        ProductDetailScreenCompose(
                            viewModel = vm,
                            product = product,
                            currentUserId = session.getEmail() ?: "guest",
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // CARRITO
                    composable("cart/{userId}") { entry ->

                        val userId = entry.arguments?.getString("userId") ?: "guest"

                        CartScreenCompose(
                            userId = userId,
                            onBack = { navController.popBackStack() },
                            onPay = {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Pago realizado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}
