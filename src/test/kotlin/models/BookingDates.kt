package models

import kotlinx.serialization.Serializable

@Serializable
data class BookingDates(val checkin: String, val checkout: String)
