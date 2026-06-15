package com.francotte.myrecipesstorekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform