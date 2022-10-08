package com.example.tsquared.NewsWebView

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.core.content.edit
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.defaultSharedPreferences

fun Context.isOnline() = connectivityManager.activeNetworkInfo?.isConnected == true

fun Context.getPrefBoolean(key: String, defValue: Boolean) =
    defaultSharedPreferences.getBoolean(key, defValue)

fun Context.putPrefBoolean(key: String, value: Boolean) =
    defaultSharedPreferences.edit { putBoolean(key, value) }

fun Context.getPrefString(key: String, defValue: String) =
    defaultSharedPreferences.getString(key, defValue)