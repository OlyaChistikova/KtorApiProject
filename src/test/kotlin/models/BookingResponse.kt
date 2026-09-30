package models

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponse(val bookingid: Long, val booking: Booking)
