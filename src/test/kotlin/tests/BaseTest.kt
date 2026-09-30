package tests

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import models.Auth
import models.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {
    protected var token: String = ""

    protected val client = HttpClient(CIO) {
        defaultRequest {
            url("https://restful-booker.herokuapp.com/")
            contentType(ContentType.Application.Json)
        }
        install(Logging) { logger = Logger.SIMPLE; level = LogLevel.INFO }
        install(HttpTimeout) { connectTimeoutMillis = 5000; requestTimeoutMillis = 15000 }
        install(ContentNegotiation) {
            json(Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true })
        }
    }

    @BeforeEach
    fun authSetUp() {
        runBlocking {
            val response = client.post("auth") {
                setBody(Auth(username = "admin", password = "password123"))
            }
            assertEquals(response.status, HttpStatusCode.OK)
            assertNotNull(response.body())
            token = Json.decodeFromString<Token>(response.bodyAsText()).token
        }
    }
}