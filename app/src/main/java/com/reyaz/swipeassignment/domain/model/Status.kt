package com.reyaz.swipeassignment.domain.model

import androidx.compose.ui.graphics.Color

enum class Status(val color: Color) {
    Pending(Color(0xFFFFC107)),
    Uploaded(Color(0xFF4CAF50)),
    Failed(Color(0xFFEF0000))
}