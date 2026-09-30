package tests

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import models.BookingId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.collections.all
import kotlin.collections.isNotEmpty

class GetBookingIdsTest: BaseBookingTest() {

    @Test
    @DisplayName("GET /booking возвращает непустой список id")
    fun `get all booking ids`() = runBlocking {
        val response = client.get("booking")
        assertEquals(HttpStatusCode.OK, response.status)
        val ids = Json.decodeFromString<List<BookingId>>(response.bodyAsText())
        assertTrue(ids.isNotEmpty())
        assertTrue(ids.all { it.bookingid > 0 })
        assertTrue(ids.any { it.bookingid == createdBookingId })
    }

    @Test
    @DisplayName("GET /booking с фильтром по имени и фамилии возвращает id бронирования")
    fun `filter by firstname and lastname`() = runBlocking {
        val response = client.get("booking"){
            parameter("firstname", originalBooking.firstname)
            parameter("lastname", originalBooking.lastname)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val ids = Json.decodeFromString<List<BookingId>>(response.bodyAsText())
        assertTrue(ids.isNotEmpty())
        assertTrue(ids.any { it.bookingid == createdBookingId })
    }

    @Test
    @DisplayName("GET /booking с фильтром по дате заезда и выезда возвращает id бронирования")
    fun `filter by checkin and checkout`() = runBlocking {
        val response = client.get("booking") {
            parameter("checkin", "0001-01-01")
            parameter("checkout", "9999-12-31")
        }
        assertEquals(HttpStatusCode.OK, response.status)

        val ids = Json.decodeFromString<List<BookingId>>(response.bodyAsText())
        assertTrue(ids.isNotEmpty())
        assertTrue(ids.any { it.bookingid == createdBookingId })
    }
}