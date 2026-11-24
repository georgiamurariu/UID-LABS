package com.example.laboratory_uid.offers

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.example.laboratory_uid.R

data class Offer(
    val id: Int,
    val destination: String,
    val nights: Int,
    @param:DrawableRes val imageRes: Int,
    val priceEur: Int?,
    val description: String,
    val fullDescription: String
) {
    val title: String get() = "$destination, $nights nights"
    val priceLabel: String get() = priceEur?.let { "$it EUR" } ?: "Price on request"
}

// Lab 7 (MVVM): datele și căutarea după ID sunt separate de interfață.
class OffersViewModel : ViewModel() {
    val offers = listOf(
        Offer(
            id = 1,
            destination = "Barcelona",
            nights = 3,
            imageRes = R.drawable.offer_1,
            priceEur = 300,
            description = "Explore Barcelona’s lively squares and Gaudí’s architecture.",
            fullDescription = "Spend three nights in Barcelona. Stroll through Plaça Reial and " +
                "the Gothic Quarter, visit the Sagrada Família, and take a walk along the waterfront."
        ),
        Offer(
            id = 2,
            destination = "Maldive",
            nights = 7,
            imageRes = R.drawable.offer_2,
            priceEur = 1050,
            description = "Relax on white-sand beaches beside a turquoise lagoon.",
            fullDescription = "Spend seven nights in the Maldives. Swim in the lagoon, " +
                "snorkel near the reef, and watch the sunset from the island’s wooden pier."
        ),
        Offer(
            id = 3,
            destination = "Thailand",
            nights = 10,
            imageRes = R.drawable.offer_3,
            priceEur = null,
            description = "Discover Thailand’s coastal scenery by long-tail boat.",
            fullDescription = "Spend ten nights in Thailand. Take a long-tail boat past limestone " +
                "cliffs, explore quiet beaches, and sample local dishes at coastal markets."
        )
    )

    fun findOffer(id: Int): Offer? = offers.firstOrNull { it.id == id }
}
