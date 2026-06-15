package com.francotte.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import com.francotte.datastore.model.UserPreferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

/** Okio-based, multiplatform serializer for [UserPreferences] (JSON via kotlinx-serialization). */
object UserPreferencesSerializer : OkioSerializer<UserPreferences> {

    override val defaultValue: UserPreferences = UserPreferences()

    override suspend fun readFrom(source: BufferedSource): UserPreferences =
        try {
            json.decodeFromString(UserPreferences.serializer(), source.readUtf8())
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read UserPreferences.", exception)
        }

    override suspend fun writeTo(t: UserPreferences, sink: BufferedSink) {
        sink.writeUtf8(json.encodeToString(UserPreferences.serializer(), t))
    }
}
