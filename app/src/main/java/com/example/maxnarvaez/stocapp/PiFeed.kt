package com.example.maxnarvaez.stocapp

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_pi_feed.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PiFeed : AppCompatActivity() {
    private val feeds = mapOf(
        1 to "http://$feed1IP/html/cam_pic_new.php?pDelay=$feedRefreshRate",
        2 to "http://$feed2IP/html/cam_pic_new.php?pDelay=$feedRefreshRate",
        3 to "http://$feed3IP/html/cam_pic_new.php?pDelay=$feedRefreshRate",
        4 to "http://$feed4IP/html/cam_pic_new.php?pDelay=$feedRefreshRate"
    )

    private lateinit var mDetector: GestureDetectorCompat

    private val mjpegViewHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Log.d("MJPEG Viewer : ", msg.obj.toString())

            when (msg.obj.toString()) {
                "DISCONNECTED" -> {// When video stream disconnected
                    findViewById<View>(R.id.feed_loading).visibility = View.VISIBLE
                }
                "CONNECTION_PROGRESS" -> {// When connection progress
                    findViewById<View>(R.id.feed_loading).visibility = View.VISIBLE
                }
                "CONNECTED" -> {// When video streaming connected
                    findViewById<ProgressBar>(R.id.feed_loading).visibility = View.INVISIBLE
                    findViewById<TextView>(R.id.feed_id).setBackgroundColor(Color.TRANSPARENT)
                }
                "CONNECTION_ERROR" -> {// When connection error
                    findViewById<View>(R.id.feed_loading).visibility = View.INVISIBLE
                    findViewById<TextView>(R.id.feed_id).setBackgroundColor(Color.RED)
                }
                "STOPPING_PROGRESS" -> {// When MjpegViewer is in stopping progress
                    findViewById<View>(R.id.feed_loading).visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pi_feed)
        mDetector = GestureDetectorCompat(this, MyGestureListener())
        startFeed()
        videoFeedView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.feed_container, SpeechFragment())
            .commit()
    }

    override fun onResume() {
        super.onResume()
        videoFeedView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        GlobalScope.launch { videoFeedView.Start(feeds[feedChoice]) }
    }

    override fun onPause() {
        super.onPause()
        Log.d("PI Feed", "Paused")
        GlobalScope.launch { videoFeedView.Stop() }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch { videoFeedView.Stop() }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val temp = feedChoice
        mDetector.onTouchEvent(event)
        if (feedChoice != temp) {
            GlobalScope.launch {
                swapFeed()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startFeed() {
        Log.d("New Feed", "$feedChoice")
        videoFeedView.Start(feeds[feedChoice], mjpegViewHandler)
        findViewById<TextView>(R.id.feed_id).text = feedChoice.toString()
    }

    private fun swapFeed() {
        videoFeedView.Stop()
        startFeed()
    }

    private class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {
            Log.d("Gesture: ", "onDown: $event")
            return true
        }

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d("Gesture: ", "onFling: $event1 $event2")
            when {
                event1.x < event2.x -> when (feedChoice) {
                    1 -> feedChoice = 4
                    else -> feedChoice--
                }
                event1.x > event2.x -> when (feedChoice) {
                    4 -> feedChoice = 1
                    else -> feedChoice++
                }
            }
            return true
        }
    }


}
