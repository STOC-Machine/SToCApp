package com.example.maxnarvaez.stocapp

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v14.preference.MultiSelectListPreference
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
        val feed5Preference = findPreference("feed_ip_5") as EditTextPreference
        val feed6Preference = findPreference("feed_ip_6") as EditTextPreference
        val feedRefreshPref = findPreference("feed_refresh") as ListPreference
        val feedSelectPref = findPreference("feed_select") as MultiSelectListPreference
        Log.d("Feed Select", feedSelectPref.values.toString())
        Log.d("Settings Startup", "Found preferences successfully")

        feed1Preference.text = feed1IP
        feed2Preference.text = feed2IP
        feed3Preference.text = feed3IP
        feed4Preference.text = feed4IP
        feed5Preference.text = feed5IP
        feed6Preference.text = feed6IP
        feedRefreshPref.value = feedRefreshRate.toString()

        Log.d("Settings Startup", "Set preferences successfully")

        val preferenceListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue =
                if (preference === feedSelectPref) (value as HashSet<*>).toList().sortedBy { it.toString() }
                    .toString().removePrefix("[").removeSuffix("]") else value as String
            preference.summary = stringValue
            val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
            when (preference) {
                feed1Preference -> {
                    feed1IP = stringValue
                    preferencesEditor.putString(FEED_IP_1_KEY, stringValue).apply()
                }
                feed2Preference -> {
                    feed2IP = stringValue
                    preferencesEditor.putString(FEED_IP_2_KEY, stringValue).apply()
                }
                feed3Preference -> {
                    feed3IP = stringValue
                    preferencesEditor.putString(FEED_IP_3_KEY, stringValue).apply()
                }
                feed4Preference -> {
                    feed4IP = stringValue
                    preferencesEditor.putString(FEED_IP_4_KEY, stringValue).apply()
                }
                feed5Preference -> {
                    feed5IP = stringValue
                    preferencesEditor.putString(FEED_IP_5_KEY, stringValue).apply()
                }
                feed6Preference -> {
                    feed6IP = stringValue
                    preferencesEditor.putString(FEED_IP_6_KEY, stringValue).apply()
                }
                feedRefreshPref -> {
                    feedRefreshRate = stringValue.toInt()
                    preferencesEditor.putInt(FEED_REFRESH_KEY, stringValue.toInt()).apply()
                }
                feedSelectPref -> {
                    preferencesEditor.putStringSet(
                        FEED_SELECT_KEY,
                        mutableSetOf<String>().apply {
                            (value as HashSet<*>).forEach {
                                this.add(it as String)
                            }
                        }
                    ).apply()
                    feedSelection.clear()
                    if (stringValue != "[]" && stringValue != "") {
                        for (v in stringValue.split(',')) feedSelection.add(
                            v.removePrefix(" ")
                                .removePrefix("Feed ")
                                .toInt()
                        )
                        feedSelection.sort()
                    }
                }
            }
            Log.d(
                "PrefListener",
                "$feed1IP, $feed2IP, $feed3IP, $feed4IP, $feed5IP, $feed6IP, $feedRefreshRate, $feedSelection"
            )
            true
        }


        feed1Preference.summary = feed1Preference.text
        feed2Preference.summary = feed2Preference.text
        feed3Preference.summary = feed3Preference.text
        feed4Preference.summary = feed4Preference.text
        feed5Preference.summary = feed5Preference.text
        feed6Preference.summary = feed6Preference.text
        feedRefreshPref.summary = feedRefreshPref.value.toString()
        feedSelectPref.summary = (feedSelectPref.values).toList().sortedBy { it.toString() }
            .toString().removePrefix("[").removeSuffix("]")
        Log.d("Settings Setup", "Set summaries successfully")
        feed1Preference.onPreferenceChangeListener = preferenceListener
        feed2Preference.onPreferenceChangeListener = preferenceListener
        feed3Preference.onPreferenceChangeListener = preferenceListener
        feed4Preference.onPreferenceChangeListener = preferenceListener
        feed5Preference.onPreferenceChangeListener = preferenceListener
        feed6Preference.onPreferenceChangeListener = preferenceListener
        feedRefreshPref.onPreferenceChangeListener = preferenceListener
        feedSelectPref.onPreferenceChangeListener = preferenceListener

    }
}