package com.francotte.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverters {

    @TypeConverter
    fun instantToLong(value: Instant?): Long? = value?.toEpochMilliseconds()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let(Instant::fromEpochMilliseconds)
}
