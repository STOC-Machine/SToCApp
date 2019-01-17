package com.example.maxnarvaez.stocapp

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.util.Log
import com.takisoft.fix.support.v7.preference.EditTextPreference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
        Log.d("Settings Startup", "Loaded preferences successfully")
        val feed1Preference = findPreference("feed_ip_1") as EditTextPreference
        val feed2Preference = findPreference("feed_ip_2") as EditTextPreference
        val feed3Preference = findPreference("feed_ip_3") as EditTextPreference
        val feed4Preference = findPreference("feed_ip_4") as EditTextPreference
        val feedRefreshPref = findPreference("feed_refresh") as ListPreference
        Log.d("Settings Startup", "Found preferences successfully")

        feed1Preference.text = feed1IP
        feed2Preference.text = feed2IP
        feed3Preference.text = feed3IP
        feed4Preference.text = feed4IP
        feedRefreshPref.value = feedRefreshRate.toString()
        Log.d("Settings Startup", "Set preferences successfully")

        val preferenceListener = Preference.OnPreferenceChangeListener { preference, value ->
            preference.summary = value as String
            val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
            when (preference) {
                feed1Preference -> {
                    feed1IP = value
                    preferencesEditor.putString(FEED_IP_1_KEY, value).apply()
                }
                feed2Preference -> {
                    feed2IP = value
                    preferencesEditor.putString(FEED_IP_2_KEY, value).apply()
                }
                feed3Preference -> {
                    feed3IP = value
                    preferencesEditor.putString(FEED_IP_3_KEY, value).apply()
                }
                feed4Preference -> {
                    feed4IP = value
                    preferencesEditor.putString(FEED_IP_4_KEY, value).apply()
                }
                feedRefreshPref -> {
                    feedRefreshRate = value.toInt()
                    preferencesEditor.putInt(FEED_REFRESH_KEY, value.toInt()).apply()
                }
            }
            Log.d(
                "PrefListener",
                "$feed1IP, $feed2IP, $feed3IP, $feed4IP, $feedRefreshRate"
            )
            true
        }


        feed1Preference.summary = feed1Preference.text
        feed2Preference.summary = feed2Preference.text
        feed3Preference.summary = feed3Preference.text
        feed4Preference.summary = feed4Preference.text
        feedRefreshPref.summary = feedRefreshPref.value.toString()
        Log.d("Settings Setup", "Set summaries successfully")
        feed1Preference.onPreferenceChangeListener = preferenceListener
        feed2Preference.onPreferenceChangeListener = preferenceListener
        feed3Preference.onPreferenceChangeListener = preferenceListener
        feed4Preference.onPreferenceChangeListener = preferenceListener
        feedRefreshPref.onPreferenceChangeListener = preferenceListener

    }
}