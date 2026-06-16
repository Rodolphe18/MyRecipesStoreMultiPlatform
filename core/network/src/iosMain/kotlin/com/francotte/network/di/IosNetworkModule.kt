package com.francotte.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

/** iOS network DI: provides the Ktor [HttpClient] backed by the Darwin engine. */
val iosNetworkModule = module {
    single {
        HttpClient(Darwin) {
            expectSuccess = true
            install(ContentNegotiation) { json(get()) }
            install(Logging) { level = LogLevel.INFO }
            install(HttpTimeout) {
                connectTimeoutMillis = 15_000
                requestTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }
        }
    }
}
