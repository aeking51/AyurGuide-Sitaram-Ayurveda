package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.ui.theme.AppThemeMode

/**
 * Manages persistent storage of the user's selected visual theme mode
 * (Classic Light, Glass Theme, or Dark Theme).
 *
 * Ensures that the selected theme applies permanently across the entire
 * application and survives app restarts, process recreation, and user sessions
 * until the user explicitly changes it in the profile screen.
 */
object ThemePreferences {
    private const val PREFS_NAME = "ayurveda_theme_preferences"
    private const val KEY_THEME_MODE = "saved_app_theme_mode"

    private var sharedPreferences: SharedPreferences? = null

    /**
     * Initializes the SharedPreferences instance using the application context.
     * Safe to call multiple times; idempotently retains the application context.
     */
    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    /**
     * Reads the permanently stored visual theme. Defaults to [AppThemeMode.LIGHT]
     * if no preference has been saved yet or if an invalid value is encountered.
     */
    fun getThemeMode(): AppThemeMode {
        val savedName = sharedPreferences?.getString(KEY_THEME_MODE, AppThemeMode.LIGHT.name)
            ?: AppThemeMode.LIGHT.name
        return try {
            AppThemeMode.valueOf(savedName)
        } catch (e: Exception) {
            AppThemeMode.LIGHT
        }
    }

    /**
     * Permanently commits the user's chosen theme mode to disk.
     */
    fun setThemeMode(mode: AppThemeMode) {
        sharedPreferences?.edit()?.putString(KEY_THEME_MODE, mode.name)?.apply()
    }
}
