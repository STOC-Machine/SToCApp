package com.example.maxnarvaez.stocapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_startup.*

class Startup : AppCompatActivity() {

    private val sharedPrefFile = "stocAppPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        setSupportActionBar(toolbar)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.startup_container, FeedSelect())
            .add(R.id.startup_container, SpeechFragment())
            .commit()

        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        feed1IP = mPreferences.getString(FEED_IP_1_KEY, feed1IP) ?: feed1IP
        feed2IP = mPreferences.getString(FEED_IP_2_KEY, feed2IP) ?: feed2IP
        feed3IP = mPreferences.getString(FEED_IP_3_KEY, feed3IP) ?: feed3IP
        feed4IP = mPreferences.getString(FEED_IP_4_KEY, feed4IP) ?: feed4IP
        feed5IP = mPreferences.getString(FEED_IP_5_KEY, feed5IP) ?: feed5IP
        feed6IP = mPreferences.getString(FEED_IP_6_KEY, feed6IP) ?: feed6IP
        feedRefreshRate = mPreferences.getInt(FEED_REFRESH_KEY, feedRefreshRate)
        val feedSelectPrefs = mPreferences.getStringSet(FEED_SELECT_KEY, null)?.toString() ?: ""
        feedSelection.clear()
        if (feedSelectPrefs != "[]" && feedSelectPrefs != "") {
            for (v in feedSelectPrefs.split(',')) feedSelection.add(
                v.removePrefix("[")
                    .removeSuffix("]")
                    .removePrefix(" ")
                    .removePrefix("Feed ")
                    .toInt()
            )
            feedSelection.sort()
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_startup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        mPreferences.edit()
            .putString(FEED_IP_1_KEY, feed1IP)
            .putString(FEED_IP_2_KEY, feed2IP)
            .putString(FEED_IP_3_KEY, feed3IP)
            .putString(FEED_IP_4_KEY, feed4IP)
            .putString(FEED_IP_5_KEY, feed5IP)
            .putString(FEED_IP_6_KEY, feed6IP)
            .putInt(FEED_REFRESH_KEY, feedRefreshRate)
            .apply()
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}
