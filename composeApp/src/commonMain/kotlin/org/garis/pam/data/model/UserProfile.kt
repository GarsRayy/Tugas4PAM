package org.garis.pam.data.model

data class UserProfile(
    val name: String,
    val initials: String,
    val badge: String,
    val subtitle: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String
)

val myProfile = UserProfile(
    name     = "Garis Rayya Rabbani",
    initials = "GR",
    badge    = "✦ Mahasiswa Aktif",
    subtitle = "Teknik Informatika 2023 · Mobile Developer",
    bio      = "Halo! Saya adalah mahasiswa Teknik Informatika yang sedang mendalami " +
            "dunia mobile development. Saat ini saya sedang belajar membangun " +
            "aplikasi lintas platform menggunakan Kotlin dan Compose Multiplatform.",
    email    = "garisrayya@gmail.com",
    phone    = "0895423021051",
    location = "Bandar Lampung, Lampung"
)
