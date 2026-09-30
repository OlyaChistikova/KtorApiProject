package tests

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PingTest: BaseTest() {
    @Test
    @DisplayName("GET /ping возвращает 201 Created")
    fun ping() = runBlocking {
        val response = client.get("ping")
        assertEquals(HttpStatusCode.Created, response.status)
    }
}