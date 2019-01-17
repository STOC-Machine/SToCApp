package com.example.maxnarvaez.stocapp

import android.content.SharedPreferences

var feedChoice = 1
var feed1IP = "0.0.0.0"
var feed2IP = "0.0.0.0"
var feed3IP = "0.0.0.0"
var feed4IP = "0.0.0.0"
var feedRefreshRate = 25000
const val FEED_IP_1_KEY = "feed_ip_1"
const val FEED_IP_2_KEY = "feed_ip_2"
const val FEED_IP_3_KEY = "feed_ip_3"
const val FEED_IP_4_KEY = "feed_ip_4"
const val FEED_REFRESH_KEY = "feed_refresh"
lateinit var mPreferences: SharedPreferences