package com.shegs.libtest

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$") // Basic email regex pattern
    return emailRegex.matches(email)
}

fun validatePassword(password: String): String? {
    val alphanumericRegex = Regex("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)\$")
    return when {
        password.length < 8 -> "Must be at least 8 characters"
        !alphanumericRegex.matches(password) -> "Password must be alphanumeric"
        else -> null
    }
}