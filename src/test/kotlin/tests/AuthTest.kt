package tests

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import models.Auth
import models.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AuthTest: BaseTest() {

    @Test
    @DisplayName("POST /auth с валидными кредами возвращает токен")
    fun `auth with valid credentials returns token`() = runBlocking {
        val response = client.post("auth") {
            setBody(Auth(username = "admin", password = "password123"))
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val tokenResponse = Json.decodeFromString<Token>(response.bodyAsText())
        assertTrue(tokenResponse.token.isNotEmpty())
    }

    @Test
    @DisplayName("POST /auth с неверными кредами возвращает Bad credentials")
    fun `auth with invalid credentials returns bad credentials`() = runBlocking {
        val response = client.post("auth") {
            setBody(Auth(username = "wrong", password = "wrong"))
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val bodyText = response.bodyAsText()
        assertTrue(bodyText.contains("Bad credentials"))
    }
}