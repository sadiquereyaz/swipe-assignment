package com.reyaz.swipeassignment.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities?.run {
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false
}
