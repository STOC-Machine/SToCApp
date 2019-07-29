package com.example.maxnarvaez.stocapp

import android.content.SharedPreferences

var feedChoice = 1
var feed1IP = "0.0.0.0"
var feed2IP = "0.0.0.0"
var feed3IP = "0.0.0.0"
var feed4IP = "0.0.0.0"
var feed5IP = "0.0.0.0"
var feed6IP = "0.0.0.0"
var feedRefreshRate = 25000
var feedSelection = mutableListOf<Int>()
var parserIP = "0.0.0.0"
var parserPort = 12459
var sendTriggers = mutableListOf<String>()
const val FEED_IP_1_KEY = "feed_ip_1"
const val FEED_IP_2_KEY = "feed_ip_2"
const val FEED_IP_3_KEY = "feed_ip_3"
const val FEED_IP_4_KEY = "feed_ip_4"
const val FEED_IP_5_KEY = "feed_ip_5"
const val FEED_IP_6_KEY = "feed_ip_6"
const val FEED_REFRESH_KEY = "feed_refresh"
const val FEED_SELECT_KEY = "feed_select"
const val PARSER_IP_KEY = "parser_ip"
const val SEND_TRIG_KEY = "send_trigs"
lateinit var mPreferences: SharedPreferences

var host = "10.42.235.221"
