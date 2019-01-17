package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class FeedSelect : Fragment() {

    lateinit var feedButton1: Button
    lateinit var feedButton2: Button
    lateinit var feedButton3: Button
    lateinit var feedButton4: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_feed_select, container, false)

        feedButton1 = thisView.findViewById<View>(R.id.feedButton1) as Button
        feedButton2 = thisView.findViewById<View>(R.id.feedButton2) as Button
        feedButton3 = thisView.findViewById<View>(R.id.feedButton3) as Button
        feedButton4 = thisView.findViewById<View>(R.id.feedButton4) as Button

        mapOf(
            1 to feedButton1,
            2 to feedButton2,
            3 to feedButton3,
            4 to feedButton4
        ).forEach { i, b ->
            b.setOnClickListener {
                openFeed(i)
            }
        }

        return thisView
    }

    private fun openFeed(feed: Int) {
        feedChoice = feed
        val intent = Intent(this.context, PiFeed::class.java)
        startActivity(intent)
    }

}
