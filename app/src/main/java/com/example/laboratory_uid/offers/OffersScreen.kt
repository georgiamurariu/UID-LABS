package com.example.laboratory_uid.offers

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    offers: List<Offer>,
    onOfferClick: (Offer) -> Unit,
    onSignOut: () -> Unit
) {
    var showSignOutDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    // Lab 7: Scaffold așază bara de sus și calculează spațiul pentru conținut.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Android Lab") },
                actions = {
                    TextButton(onClick = { showSignOutDialog = true }) {
                        Text("SIGN OUT")
                    }
                    OverflowMenu("More options") { dismiss ->
                        DropdownMenuItem(
                            text = { Text("Offer count") },
                            onClick = {
                                dismiss()
                                // Toast-ul afișează pentru scurt timp numărul de oferte.
                                Toast.makeText(
                                    context,
                                    "${offers.size} travel offers available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Lista derulabilă folosește padding-ul primit de la Scaffold.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(offers, key = { it.id }) { offer ->
                OfferItem(offer = offer, onClick = { onOfferClick(offer) })
            }
        }
    }

    // Doar confirmarea din dialog apelează deconectarea.
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text("Sign out?") },
            text = { Text("Do you want to return to the sign-in screen?") },
            confirmButton = {
                TextButton(onClick = {
                    showSignOutDialog = false
                    onSignOut()
                }) {
                    Text("SIGN OUT")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }
}

// Același element reutilizabil afișează fiecare ofertă din listă.
@Composable
fun OfferItem(offer: Offer, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClickLabel = "View details", onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = offer.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            OverflowMenu("Options for ${offer.destination}") { dismiss ->
                DropdownMenuItem(
                    text = { Text("View details") },
                    onClick = {
                        dismiss()
                        onClick()
                    }
                )
            }
        }
        OfferImage(offer)
        Text(
            text = offer.description,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferDetailsScreen(offer: Offer?, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offer details") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (offer == null) {
                Text("This offer is unavailable.")
            } else {
                Text(
                    text = offer.destination,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("${offer.nights} nights", style = MaterialTheme.typography.titleMedium)
                OfferImage(offer)
                Text(offer.fullDescription, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
// Lista și detaliile refolosesc aceeași imagine cu prețul suprapus.
private fun OfferImage(offer: Offer) {
    Box(modifier = Modifier.fillMaxWidth().aspectRatio(3f)) {
        Image(
            painter = painterResource(offer.imageRes),
            contentDescription = offer.destination,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0.45f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.75f)
                    )
                )
        )
        Text(
            text = offer.priceLabel,
            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
// Bara de sus și fiecare ofertă folosesc același meniu cu trei puncte.
private fun OverflowMenu(
    label: String,
    content: @Composable (dismiss: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.semantics { contentDescription = label }
        ) {
            val color = LocalContentColor.current
            Canvas(modifier = Modifier.size(24.dp)) {
                for (dot in -1..1) {
                    drawCircle(
                        color = color,
                        radius = 2.dp.toPx(),
                        center = center + Offset(0f, dot * 6.dp.toPx())
                    )
                }
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            content { expanded = false }
        }
    }
}
