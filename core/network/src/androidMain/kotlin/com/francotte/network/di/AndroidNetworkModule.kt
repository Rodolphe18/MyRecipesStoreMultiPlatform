package com.francotte.network.di

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Dns
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.net.InetAddress
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/** Custom DNS pinning the MyRecipesStore backend to a known host. */
private val foodDns = object : Dns {
    override fun lookup(hostname: String): List<InetAddress> =
        if (hostname == "app.myrecipesstore18.com" || hostname == "myrecipesstore18.com") {
            listOf(InetAddress.getByName("46.202.170.205"))
        } else {
            Dns.SYSTEM.lookup(hostname)
        }
}

/** Android network DI: provides the Ktor [HttpClient] backed by an OkHttp engine. */
val androidNetworkModule = module {
    single {
        createHttpClient(
            context = androidContext(),
            json = get(),
            sharedExecutor = get(),
        )
    }
}

private fun createHttpClient(
    context: Context,
    json: Json,
    sharedExecutor: ExecutorService,
): HttpClient =
    HttpClient(OkHttp) {
        expectSuccess = true
        install(ContentNegotiation) { json(json) }
        install(Logging) { level = LogLevel.BODY }
        install(HttpTimeout) {
            connectTimeoutMillis = 15_000
            requestTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
        }
        engine {
            config {
                dns(foodDns)
                cache(Cache(File(context.cacheDir, "http_cache"), CACHE_SIZE_BYTES))
                connectionPool(ConnectionPool(10, 5, TimeUnit.MINUTES))
                dispatcher(Dispatcher(sharedExecutor))
            }
        }
    }

private const val CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10 MB
