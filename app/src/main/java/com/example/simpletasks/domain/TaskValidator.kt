package com.example.simpletasks.domain

object TaskValidator {
    fun isTitleValid(title: String): Boolean {
        return title.trim().length in 1..50
    }

    fun sanitizeDescription(description: String): String {
        return description.trim()
    }
}
