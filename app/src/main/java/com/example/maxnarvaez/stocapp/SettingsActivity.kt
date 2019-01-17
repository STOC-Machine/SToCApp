package com.example.maxnarvaez.stocapp

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.util.Log
import com.takisoft.fix.support.v7.preference.EditTextPreference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()
    }


}

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
        val feed1Preference = findPreference("feed_ip_1") as EditTextPreference
        val feed2Preference = findPreference("feed_ip_2") as EditTextPreference
        val feed3Preference = findPreference("feed_ip_3") as EditTextPreference
        val feed4Preference = findPreference("feed_ip_4") as EditTextPreference

        feed1Preference.text = feed1IP
        feed2Preference.text = feed2IP
        feed3Preference.text = feed3IP
        feed4Preference.text = feed4IP


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
            }
            Log.d("PrefListener", "$feed1IP, $feed2IP, $feed3IP, $feed4IP")
            true
        }


        feed1Preference.summary = feed1Preference.text
        feed2Preference.summary = feed2Preference.text
        feed3Preference.summary = feed3Preference.text
        feed4Preference.summary = feed4Preference.text
        feed1Preference.onPreferenceChangeListener = preferenceListener
        feed2Preference.onPreferenceChangeListener = preferenceListener
        feed3Preference.onPreferenceChangeListener = preferenceListener
        feed4Preference.onPreferenceChangeListener = preferenceListener

    }
}
