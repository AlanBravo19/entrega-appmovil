package com.example.appmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appmovil.data.SessionManager
import com.example.appmovil.ui.cart.CartScreenCompose
import com.example.appmovil.ui.cart.OrderSummaryScreen
import com.example.appmovil.ui.cart.PurchaseCompleteScreenCompose
import com.example.appmovil.ui.cart.PurchaseInfo
import com.example.appmovil.ui.cart.OrderHistoryScreen
import com.example.appmovil.ui.detail.ProductDetailScreenCompose
import com.example.appmovil.ui.detail.ProductDetailViewModel
import com.example.appmovil.ui.home.HomeScreenCompose
import com.example.appmovil.ui.home.HomeViewModel
import com.example.appmovil.ui.login.LoginScreenCompose
import com.example.appmovil.ui.login.LoginViewModel
import com.example.appmovil.ui.login.RegisterScreenCompose
import com.example.appmovil.ui.login.RegisterViewModel
import com.example.appmovil.ui.profile.ProfileScreenCompose
import com.example.appmovil.ui.profile.ProfileViewModel
import com.example.appmovil.ui.theme.AppMovilTheme
import com.example.appmovil.ui.ui.domain.model.Product
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppMovilTheme {

                val session = SessionManager(this)
                val navController = rememberNavController()

                // 🔥 SI YA ESTÁ LOGEADO → IR DIRECTO AL HOME
                val startDestination = if (session.isLoggedIn()) "home" else "login"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {

                    // -------------------------------
                    // LOGIN
                    // -------------------------------
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

                    // -------------------------------
                    // REGISTER
                    // -------------------------------
                    composable("register") {
                        val vm = RegisterViewModel(session)
                        RegisterScreenCompose(
                            viewModel = vm,
                            onRegisterSuccess = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // -------------------------------
                    // HOME
                    // -------------------------------
                    composable("home") {
                        val homeVM: HomeViewModel = viewModel()
                        HomeScreenCompose(
                            viewModel = homeVM,
                            onLogout = {
                                session.logout()
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onProductClick = { product ->
                                val json = Gson().toJson(product)
                                val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                                navController.navigate("productDetail/$encoded")
                            },
                            onCartClick = {
                                val userId = session.getEmail() ?: "guest"
                                navController.navigate("cart/$userId")
                            },
                            onHistoryClick = {
                                navController.navigate("orderHistory")
                            },
                            onUserClick = {
                                navController.navigate("profile")
                            }
                        )
                    }

                    // -------------------------------
                    // PROFILE
                    // -------------------------------
                    composable("profile") {
                        val vm = ProfileViewModel(session)
                        ProfileScreenCompose(
                            viewModel = vm,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // -------------------------------
                    // PRODUCT DETAIL
                    // -------------------------------
                    composable("productDetail/{json}") { entry ->
                        val encodedJson = entry.arguments?.getString("json") ?: ""
                        val decoded = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
                        val product = Gson().fromJson(decoded, Product::class.java)
                        val vm: ProductDetailViewModel = viewModel()

                        ProductDetailScreenCompose(
                            viewModel = vm,
                            product = product,
                            currentUserId = session.getEmail() ?: "guest",
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // -------------------------------
                    // CART
                    // -------------------------------
                    composable("cart/{userId}") { entry ->
                        val userId = entry.arguments?.getString("userId") ?: "guest"
                        CartScreenCompose(
                            userId = userId,
                            onBack = { navController.popBackStack() },
                            navController = navController
                        )
                    }

                    // -------------------------------
                    // ORDER SUMMARY
                    // -------------------------------
                    composable(
                        route = "orderSummary/{total}/{productsEncoded}/{userId}",
                        arguments = listOf(
                            navArgument("total") { type = NavType.StringType },
                            navArgument("productsEncoded") { type = NavType.StringType },
                            navArgument("userId") { type = NavType.StringType }
                        )
                    ) { entry ->
                        val total = entry.arguments?.getString("total") ?: "0"
                        val productsEncoded = entry.arguments?.getString("productsEncoded") ?: ""
                        val userId = entry.arguments?.getString("userId") ?: "guest"

                        OrderSummaryScreen(
                            navController = navController,
                            total = total,
                            productsEncoded = productsEncoded,
                            userId = userId
                        )
                    }

                    // -------------------------------
                    // PURCHASE COMPLETE
                    // -------------------------------
                    composable(
                        route = "purchase_complete/{total}/{products}/{userId}",
                        arguments = listOf(
                            navArgument("total") { type = NavType.StringType },
                            navArgument("products") { type = NavType.StringType },
                            navArgument("userId") { type = NavType.StringType }
                        )
                    ) { entry ->
                        val totalRaw = entry.arguments?.getString("total") ?: "0"
                        val productsEncoded = entry.arguments?.getString("products") ?: ""
                        val userId = entry.arguments?.getString("userId") ?: "guest"

                        val productsList = if (productsEncoded.isNotEmpty()) {
                            URLDecoder.decode(productsEncoded, StandardCharsets.UTF_8.toString()).split("|")
                        } else emptyList()

                        val fakeLocation = LatLng(-33.4489, -70.6693)

                        PurchaseCompleteScreenCompose(
                            navController = navController,
                            purchase = PurchaseInfo(
                                total = "$$totalRaw",
                                products = productsList,
                                location = fakeLocation,
                                userId = userId
                            ),
                            session = session
                        )
                    }

                    // -------------------------------
                    // ORDER HISTORY
                    // -------------------------------
                    composable("orderHistory") {
                        OrderHistoryScreen(
                            session = session,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
