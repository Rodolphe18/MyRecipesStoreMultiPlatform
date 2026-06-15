package com.francotte.common.counters

object ScreenCounter {
    var screenCount: Int = 0
        private set

    fun increment() {
        screenCount++
    }
}
