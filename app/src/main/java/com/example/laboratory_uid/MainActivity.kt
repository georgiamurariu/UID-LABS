package com.example.laboratory_uid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.laboratory_uid.authScreen.AuthScreen
import com.example.laboratory_uid.offers.OfferDetailsScreen
import com.example.laboratory_uid.offers.OffersScreen
import com.example.laboratory_uid.offers.OffersViewModel
import com.example.laboratory_uid.ui.theme.Laboratory_UIDTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Laboratory_UIDTheme {
                LabNavigation()
            }
        }
    }
}

// Lab 7: rutele tipizate descriu ecranele și argumentele lor.
@Serializable
private object LoginRoute

@Serializable
private object OffersRoute

@Serializable
private data class OfferDetailsRoute(val offerId: Int)

@Composable
private fun LabNavigation() {
    val navController = rememberNavController()
    val offersViewModel: OffersViewModel = viewModel()

    // Graful pornește de la autentificare. Fiecare composable declară un ecran.
    NavHost(navController = navController, startDestination = LoginRoute) {
        composable<LoginRoute> {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AuthScreen(
                    viewModel = viewModel(),
                    modifier = Modifier.padding(innerPadding),
                    // Scoatem ecranul de autentificare din stivă după conectare.
                    onSignedIn = {
                        navController.navigate(OffersRoute) {
                            popUpTo<LoginRoute> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable<OffersRoute> {
            OffersScreen(
                offers = offersViewModel.offers,
                onOfferClick = { offer ->
                    navController.navigate(OfferDetailsRoute(offer.id)) {
                        launchSingleTop = true
                    }
                },
                // La deconectare eliminăm ecranul de oferte din stivă.
                onSignOut = {
                    navController.navigate(LoginRoute) {
                        popUpTo<OffersRoute> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<OfferDetailsRoute> { entry ->
            // Ruta transmite ID-ul. ViewModel-ul găsește oferta completă.
            val offerId = entry.toRoute<OfferDetailsRoute>().offerId
            OfferDetailsScreen(
                offer = offersViewModel.findOffer(offerId),
                // Același efect ca butonul Back al sistemului: revenim la listă.
                onBack = { navController.popBackStack() }
            )
        }
    }
}
