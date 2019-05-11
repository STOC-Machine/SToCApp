package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment


class FeedSelect : Fragment() {

    private lateinit var feedButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_feed_select, container, false)

        feedButton = thisView.findViewById<View>(R.id.feedButton) as Button
        feedButton.setOnClickListener { openFeed() }

        return thisView
    }

    private fun openFeed() {
        if (feedSelection.isNotEmpty()) {
            feedChoice = feedSelection.first()
            val intent = Intent(this.context, PiFeed::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this.context, "No feeds selected", Toast.LENGTH_LONG).show()
        }
    }

}
