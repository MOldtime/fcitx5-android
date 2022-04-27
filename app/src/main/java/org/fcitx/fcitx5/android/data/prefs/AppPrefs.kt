package org.fcitx.fcitx5.android.data.prefs

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceScreen
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.input.candidates.expanded.ExpandedCandidateStyle

class AppPrefs(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources
) {

    private val providers = mutableListOf<ManagedPreferenceProvider>()
    private val managedPreferences = mutableMapOf<String, ManagedPreference<*, *>>()

    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            managedPreferences[key]?.fireChange()
        }

    private fun <T : ManagedPreferenceProvider> T.register() = apply {
        registerProvider { this }
    }

    inner class Internal : ManagedPreferenceInternal(sharedPreferences) {
        val firstRun = bool("first_run", true)
        val lastSymbolLayout = string("last_symbol_layout", "NumSym")
        val verboseLog = bool("verbose_log", false)
        val pid = int("pid", 0)
    }

    inner class Advanced : ManagedPreferenceCategory(R.string.advanced, sharedPreferences) {
        val ignoreSystemCursor = switch(R.string.ignore_sys_cursor, "ignore_system_cursor", true)
        val hideKeyConfig = switch(R.string.hide_key_config, "hide_key_config", true)
    }

    inner class Keyboard : ManagedPreferenceCategory(R.string.keyboard, sharedPreferences) {
        val buttonHapticFeedback =
            switch(R.string.button_haptic_feedback, "button_haptic_feedback", true)
        val expandedCandidateStyle =
            list(
                R.string.expanded_candidate_style,
                "expanded_candidate_style",
                ExpandedCandidateStyle.Grid,
                ExpandedCandidateStyle,
                listOf(
                    resources.getString(R.string.expanded_candidate_style_grid) to ExpandedCandidateStyle.Grid,
                    resources.getString(R.string.expanded_candidate_style_flexbox) to ExpandedCandidateStyle.Flexbox
                )
            )
        val keyboardHeightPercent =
            int(R.string.keyboard_height, "keyboard_height_percent", 30, 30, 70, "%")
        val keyboardHeightPercentLandscape =
            int(
                R.string.keyboard_height_landscape,
                "keyboard_height_percent_landscape",
                49,
                30,
                70,
                "%"
            )
        val expandedCandidateGridSpanCountPortrait = int(
            R.string.expanded_candidate_grid_span_count_portrait,
            "expanded_candidate_grid_span_count_portrait",
            6,
            4,
            10
        )
        val expandedCandidateGridSpanCountLandscape = int(
            R.string.expanded_candidate_grid_span_count_landscape,
            "expanded_candidate_grid_span_count_landscape",
            8,
            6,
            12
        )
        val longPressDelay = int(
            R.string.keyboard_long_press_delay,
            "keyboard_long_press_delay",
            300,
            100,
            700,
            "ms"
        )

    }

    inner class Clipboard : ManagedPreferenceCategory(R.string.clipboard, sharedPreferences) {
        val clipboardListening = switch(R.string.clipboard_listening, "clipboard_enable", true)
        val clipboardHistoryLimit = int(
            R.string.clipboard_limit,
            "clipboard_limit",
            5,
            1,
            50
        )
        val clipboardItemTimeout = int(
            R.string.clipboard_item_timeout, "clipboard_item_timeout",
            30, 30, 300, "s"
        )
    }

    val internal = Internal().register()
    val keyboard = Keyboard().register()
    val clipboard = Clipboard().register()
    val advanced = Advanced().register()

    fun createUi(screen: PreferenceScreen) {
        providers.forEach {
            it.createUi(screen)
        }
    }

    fun <T : ManagedPreferenceProvider> registerProvider(
        includeUi: Boolean = true,
        providerF: (SharedPreferences) -> T
    ): T {
        val provider = providerF(sharedPreferences)
        if (includeUi)
            providers.add(provider)
        managedPreferences.putAll(provider.managedPreferences)
        return provider
    }

    companion object {
        private var instance: AppPrefs? = null

        /**
         * MUST call before use
         */
        fun init(sharedPreferences: SharedPreferences, resources: Resources) {
            if (instance != null)
                return
            instance = AppPrefs(sharedPreferences, resources)
            sharedPreferences.registerOnSharedPreferenceChangeListener(getInstance().onSharedPreferenceChangeListener)
        }

        fun getInstance() = instance!!
    }
}