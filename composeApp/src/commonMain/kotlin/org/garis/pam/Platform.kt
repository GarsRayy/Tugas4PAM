package org.garis.pam

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform