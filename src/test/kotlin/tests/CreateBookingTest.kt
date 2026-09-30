package tests

import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import models.Booking
import models.BookingDates
import models.BookingResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CreateBookingTest: BaseTest() {
    private var createdBookingId: Long = -1

    @AfterEach
    fun cleanUp() = runBlocking {
        if (createdBookingId > 0) {
            client.delete("booking/$createdBookingId") {
                header("Cookie", "token=$token")
            }
        }
    }

    @Test
    @DisplayName("POST /booking создаёт бронь и возвращает bookingid")
    fun `create booking`() = runBlocking {
        val booking = Booking(
            firstname = "John",
            lastname = "Doe",
            totalprice = 150,
            depositpaid = true,
            bookingdates = BookingDates("2025-03-01", "2025-03-10"),
            additionalneeds = "Breakfast"
        )

        val response = client.post("booking") {
            setBody(booking)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val created = Json.decodeFromString<BookingResponse>(response.bodyAsText())
        assertTrue(created.bookingid > 0)
        assertEquals("John", created.booking.firstname)
        assertEquals("Doe", created.booking.lastname)
        assertEquals(150, created.booking.totalprice)
        assertEquals(true, created.booking.depositpaid)

        createdBookingId = created.bookingid
    }

    @Test
    @DisplayName("POST /booking без additionalneeds (опциональное поле)")
    fun `create booking without additionalneeds`() = runBlocking {
        val booking = Booking(
            firstname = "Jane",
            lastname = "Roe",
            totalprice = 100,
            depositpaid = false,
            bookingdates = BookingDates("2025-04-01", "2025-04-05")
        )

        val response = client.post("booking") {
            setBody(booking)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val created = Json.decodeFromString<BookingResponse>(response.bodyAsText())
        assertTrue(created.bookingid > 0)
        assertEquals("Jane", created.booking.firstname)

        createdBookingId = created.bookingid
    }
}