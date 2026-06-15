package com.francotte.network.model

/**
 * Platform-agnostic representation of an image to upload as multipart form data.
 *
 * Reading and resizing a platform image source (e.g. an Android `Uri`) into raw [bytes]
 * is done in the platform-specific source set, keeping the Ktor APIs in `commonMain`
 * free of any platform dependency.
 */
class ImageUpload(
    val bytes: ByteArray,
    val fileName: String,
    val contentType: String = "image/jpeg",
    val partName: String = "image",
)
