package tests

import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import models.Booking
import models.BookingDates
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UpdateBookingTest: BaseBookingTest() {

    @Test
    @DisplayName("PUT /booking/{id} обновляет бронь полностью")
    fun `update booking via put`() = runBlocking {
        val updated = Booking(
            firstname = "Peter",
            lastname = "Parker",
            totalprice = 500,
            depositpaid = false,
            bookingdates = BookingDates("2025-06-01", "2025-06-10"),
            additionalneeds = "Lunch"
        )
        val updateResponse = client.put("booking/${createdBookingId}") {
            header("Cookie", "token=$token")
            setBody(updated)
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)

        val fetched = Json.decodeFromString<Booking>(updateResponse.bodyAsText())
        assertEquals(updated.firstname, fetched.firstname)
        assertEquals(updated.totalprice, fetched.totalprice)
        assertEquals(updated.depositpaid, fetched.depositpaid)
        assertEquals(updated.bookingdates?.checkin, fetched.bookingdates?.checkin)
        assertEquals(updated.additionalneeds, fetched.additionalneeds)
    }

    @Test
    @DisplayName("PUT /booking/{id} без токена возвращает 403")
    fun `update booking without token returns 403`() = runBlocking {
        val booking = Booking(
            firstname = "Test",
            lastname = "User",
            totalprice = 100,
            depositpaid = true,
            bookingdates = BookingDates("2025-01-01", "2025-01-02")
        )

        val response = client.put("booking/${createdBookingId}") {
            setBody(booking)
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }
}