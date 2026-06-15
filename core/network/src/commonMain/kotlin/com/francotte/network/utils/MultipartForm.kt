package com.francotte.network.utils

import com.francotte.network.model.ImageUpload
import io.ktor.client.request.forms.FormBuilder
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

/** Appends an [ImageUpload] as a binary part to a Ktor multipart form. */
internal fun FormBuilder.appendImage(image: ImageUpload) {
    append(
        key = image.partName,
        value = image.bytes,
        headers = Headers.build {
            append(HttpHeaders.ContentType, image.contentType)
            append(HttpHeaders.ContentDisposition, "filename=\"${image.fileName}\"")
        },
    )
}
