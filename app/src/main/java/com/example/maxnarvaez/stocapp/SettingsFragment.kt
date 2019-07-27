package com.example.maxnarvaez.stocapp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.*

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
        Log.d("Settings Startup", "Loaded preferences successfully")
        val feed1Preference = findPreference(FEED_IP_1_KEY) as EditTextPreference
        val feed2Preference = findPreference(FEED_IP_2_KEY) as EditTextPreference
        val feed3Preference = findPreference(FEED_IP_3_KEY) as EditTextPreference
        val feed4Preference = findPreference(FEED_IP_4_KEY) as EditTextPreference
        val feed5Preference = findPreference(FEED_IP_5_KEY) as EditTextPreference
        val feed6Preference = findPreference(FEED_IP_6_KEY) as EditTextPreference
        val feedRefreshPref = findPreference(FEED_REFRESH_KEY) as ListPreference
        val feedSelectPref = findPreference(FEED_SELECT_KEY) as MultiSelectListPreference
        val parserPreference = findPreference(PARSER_IP_KEY) as EditTextPreference


        val preferences = listOf(
            feed1Preference,
            feed2Preference,
            feed3Preference,
            feed4Preference,
            feed5Preference,
            feed6Preference,
            parserPreference,
            feedRefreshPref,
            feedSelectPref
        )

        Log.d("Feed Select", feedSelectPref.values.toString())
        Log.d("Settings Startup", "Found preferences successfully")

        feed1Preference.text = feed1IP
        feed2Preference.text = feed2IP
        feed3Preference.text = feed3IP
        feed4Preference.text = feed4IP
        feed5Preference.text = feed5IP
        feed6Preference.text = feed6IP
        parserPreference.text = parserIP
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
                parserPreference -> {
                    parserIP = stringValue
                    preferencesEditor.putString(PARSER_IP_KEY, stringValue).apply()
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
        parserPreference.summary = parserPreference.text
        Log.d("Settings Setup", "Set summaries successfully")

        preferences.forEach {
            it.onPreferenceChangeListener = preferenceListener
        }
    }
}