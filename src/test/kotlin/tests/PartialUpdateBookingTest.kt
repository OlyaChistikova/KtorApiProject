package tests

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import models.Booking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PartialUpdateBookingTest: BaseBookingTest() {

    @Test
    @DisplayName("PATCH /booking/{id} частично обновляет бронь")
    fun `partial update booking`() = runBlocking {
        val originalLastname = originalBooking.lastname
        val originalCheckin = originalBooking.bookingdates?.checkin
        val originalCheckout = originalBooking.bookingdates?.checkout
        val originalDepositpaid = originalBooking.depositpaid

        val newFirstname = "Patched-First"
        val newTotalprice = 777

        val partialUpdate = buildJsonObject {
            put("firstname", newFirstname)
            put("totalprice", newTotalprice)
        }

        val patchResponse = client.patch("booking/$createdBookingId") {
            header("Cookie", "token=$token")
            setBody(partialUpdate)
        }
        assertEquals(HttpStatusCode.OK, patchResponse.status)

        val responseBooking = Json.decodeFromString<Booking>(patchResponse.bodyAsText())
        assertEquals(newFirstname, responseBooking.firstname)
        assertEquals(newTotalprice, responseBooking.totalprice)

        assertEquals(originalLastname, responseBooking.lastname)
        assertEquals(originalCheckin, responseBooking.bookingdates?.checkin)
        assertEquals(originalCheckout, responseBooking.bookingdates?.checkout)
        assertEquals(originalDepositpaid, responseBooking.depositpaid)

        val getResponse = client.get("booking/$createdBookingId")
        assertEquals(HttpStatusCode.OK, getResponse.status)

        val fetched = Json.decodeFromString<Booking>(getResponse.bodyAsText())
        assertEquals(newFirstname, fetched.firstname)
        assertEquals(newTotalprice, fetched.totalprice)
        assertEquals(originalLastname, fetched.lastname)
    }
}