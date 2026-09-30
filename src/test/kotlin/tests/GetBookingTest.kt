package tests

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import models.Booking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetBookingTest: BaseBookingTest() {

    @Test
    @DisplayName("GET /booking/{id} возвращает созданную бронь")
    fun `get booking by id`() = runBlocking {
        val response = client.get("booking/$createdBookingId")
        assertEquals(HttpStatusCode.OK, response.status)

        val fetched = Json.decodeFromString<Booking>(response.bodyAsText())

        assertEquals(originalBooking.firstname, fetched.firstname)
        assertEquals(originalBooking.lastname, fetched.lastname)
        assertEquals(originalBooking.totalprice, fetched.totalprice)
        assertEquals(originalBooking.depositpaid, fetched.depositpaid)
        assertEquals(originalBooking.bookingdates?.checkin, fetched.bookingdates?.checkin)
        assertEquals(originalBooking.bookingdates?.checkout, fetched.bookingdates?.checkout)
    }

    @Test
    @DisplayName("GET /booking/{id} с несуществующим id возвращает 404")
    fun `get non existing booking returns 404`() = runBlocking {
        val response = client.get("booking/999999999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}