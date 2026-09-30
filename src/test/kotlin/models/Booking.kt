package models

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val firstname: String? = null,
    val lastname: String? = null,
    val totalprice: Int? = null,
    val depositpaid: Boolean? = null,
    val bookingdates: BookingDates? = null,
    val additionalneeds: String? = null
)
