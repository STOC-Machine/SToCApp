package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class FeedSelect : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_select, container, false)
    }

    private fun openFeed(feed: Int) {
        feedChoice = feed
        val intent = Intent(this.context, PiFeed::class.java)
        startActivity(intent)
    }

    fun openFeed1(view: View) = openFeed(1)
    fun openFeed2(view: View) = openFeed(2)
    fun openFeed3(view: View) = openFeed(3)
    fun openFeed4(view: View) = openFeed(4)

}
