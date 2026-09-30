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
import org.junit.jupiter.api.BeforeEach
import java.util.UUID

abstract class BaseBookingTest: BaseTest() {
    protected var createdBookingId: Long = -1
    protected lateinit var originalBooking: Booking

    @BeforeEach
    fun createTestBooking() = runBlocking {
        originalBooking = Booking(
            firstname = "TestFirst-${UUID.randomUUID()}",
            lastname = "TestLast-${UUID.randomUUID()}",
            totalprice = 300,
            depositpaid = true,
            bookingdates = BookingDates("2025-01-01", "2025-01-05"),
            additionalneeds = "Test"
        )

        val createResponse = client.post("booking") {
            setBody(originalBooking)
        }
        assertEquals(HttpStatusCode.OK, createResponse.status)

        val created = Json.decodeFromString<BookingResponse>(createResponse.bodyAsText())
        createdBookingId = created.bookingid
    }

    @AfterEach
    fun deleteTestBooking() = runBlocking {
        if (createdBookingId > 0) {
            val response = client.delete("booking/$createdBookingId") {
                header("Cookie", "token=$token")
            }
            assertTrue(
                response.status == HttpStatusCode.Created ||
                        response.status == HttpStatusCode.NotFound
            )
        }
    }
}