package com.zdor.application.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {
    private val inputFormatter = DateTimeFormatter.ISO_INSTANT
    private val outputFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    fun formatDateTitle(isoDateString: String): String {
        return try {
            val instant = Instant.parse(isoDateString)
            val date = instant.atZone(ZoneId.systemDefault())
            "${date.year}-${String.format("%02d", date.monthValue)}-${String.format("%02d", date.dayOfMonth)}"
        } catch (e: Exception) {
            isoDateString
        }
    }

    fun formatDateValue(isoDateString: String): String {
        return try {
            val instant = Instant.parse(isoDateString)
            val date = instant.atZone(ZoneId.systemDefault())
            "${date.year}-${String.format("%02d", date.monthValue)}-${String.format("%02d", date.dayOfMonth)}"
        } catch (e: Exception) {
            isoDateString
        }
    }
} 