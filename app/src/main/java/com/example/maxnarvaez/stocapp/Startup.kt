package com.example.maxnarvaez.stocapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_startup.*
import kotlinx.android.synthetic.main.content_startup.*

class Startup : AppCompatActivity() {



    private val sharedPrefFile = "stocAppPrefs"


    override fun onCreate(savedInstanceState: Bundle?) {
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


//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_startup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
        preferencesEditor.putString(FEED_IP_1_KEY, feed1IP)
        preferencesEditor.putString(FEED_IP_2_KEY, feed2IP)
        preferencesEditor.putString(FEED_IP_3_KEY, feed3IP)
        preferencesEditor.putString(FEED_IP_4_KEY, feed4IP)
        preferencesEditor.apply()
    }

    private fun openFeed(feed: Int) {
        feedChoice = feed
        val intent = Intent(this, PiFeed::class.java)
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun openFeed1(view: View) = openFeed(1)
    fun openFeed2(view: View) = openFeed(2)
    fun openFeed3(view: View) = openFeed(3)
    fun openFeed4(view: View) = openFeed(4)

}
