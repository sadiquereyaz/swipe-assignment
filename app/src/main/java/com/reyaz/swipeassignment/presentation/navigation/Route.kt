package com.reyaz.swipeassignment.presentation.navigation

import kotlinx.serialization.Serializable


sealed interface Route {
    @Serializable
    data object Products : Route
    @Serializable
    data object Notification : Route
}