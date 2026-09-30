package tests

import io.ktor.client.call.body
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class DeleteBookingTest: BaseTest() {
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
    fun `post booking and then delete`() {
        var bookingId:Long
        val booking = Booking(
            "Jim", "Brown", 111, true,
            BookingDates("2020-01-01", "2019-01-01"),
            "Breakfast"
        )
        runBlocking {
            val postresponse = client.post("booking") {
                setBody(booking)
            }
            assertEquals(postresponse.status, HttpStatusCode.OK)
            assertNotNull(postresponse.body())
            bookingId =
                Json.decodeFromString<BookingResponse>(postresponse.bodyAsText()).bookingid
            val deleteResponse = client.delete("booking/$bookingId") {
                header("Cookie", "token=$token")
            }
            assertEquals(deleteResponse.status, HttpStatusCode.Created)
        }
    }
}