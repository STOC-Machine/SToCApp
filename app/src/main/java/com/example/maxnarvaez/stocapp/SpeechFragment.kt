package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class SpeechFragment : Fragment(), RecognitionListener {
    var sr: SpeechRecognizer? = null
    var speechText: TextView? = null
    var speechButton: Button? = null
    var result: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sr = SpeechRecognizer.createSpeechRecognizer(this.context)
        sr!!.setRecognitionListener(this)//this activity is the listener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val thisView = inflater.inflate(R.layout.fragment_speech, container, false)
        speechText = thisView.findViewById<View>(R.id.speechText) as TextView
        speechButton = thisView.findViewById<View>(R.id.speechButton) as Button
        speechButton!!.setOnClickListener{view ->
            startListening()
        }

        speechButton!!.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY))

        // Inflate the layout for this fragment
        return thisView
    }


    //App is ready for the user to speak
    override fun onReadyForSpeech(params: Bundle) {
        speechButton!!.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN))
    }

    //user has begun to speak
    override fun onBeginningOfSpeech() {
        speechButton!!.setBackgroundColor(Color.CYAN)
        speechButton!!.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN))
    }

    //change in volume (I believe, haven't read much on this)
    override fun onRmsChanged(rmsdB: Float) {
        //don't bother
    }

    //buffer recieved (was never triggered in all my testing)
    override fun onBufferReceived(buffer: ByteArray) {
        //don't bother
    }

    //it's been a while since they talked, they're done
    override fun onEndOfSpeech() {
        speechButton!!.setBackgroundColor(Color.GRAY)
        speechButton!!.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY))

    }

    //something went wrong
    override fun onError(error: Int) {
        var errorString: String = ""
        when (error) {
            SpeechRecognizer.ERROR_AUDIO -> errorString = "ERROR_AUDIO"
            SpeechRecognizer.ERROR_CLIENT -> errorString = "ERROR_CLIENT"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> errorString = "ERROR+INSUFFICIENT_PERMISSIONS"
            SpeechRecognizer.ERROR_NETWORK -> errorString = "ERROR_NETWORK"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> errorString = "ERROR_NETWORK_TIMEOUT"
            SpeechRecognizer.ERROR_NO_MATCH -> errorString = "ERROR_NO_MATCH"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> errorString = "ERROR_RECOGNIZER_BUSY"
            SpeechRecognizer.ERROR_SERVER -> errorString = "ERROR_SERVR"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> errorString = "ERROR_SPEECH_TIMEOUT"
            else -> {
                errorString = "ERROR_IF_YOU_SEE_THIS_SOMETHING_HAS_GONE_HORRIBLY_WRONG"
            }
        }

        println("ERROR $error")
        speechText!!.text = errorString
        //sr!!.stopListening()
        //not sure if this is how you're supposed to do it, but it works!
        onBeginningOfSpeech()
        onEndOfSpeech()
    }

    //speech has been processed, here are the results
    override fun onResults(results: Bundle) {
        var result: java.util.ArrayList<String> =
            results.get(/*sr.RESULTS_RECOGNITION*/SpeechRecognizer.RESULTS_RECOGNITION) as java.util.ArrayList<String>
        speechText!!.text = result[0]//Main one, the one it's most confident about...
        println("Possible results: $result")
    }

    //partial results? I never ran into these but maybe if it was interrupted?
    override fun onPartialResults(partialResults: Bundle) {
        var result: java.util.ArrayList<String> =
            partialResults.get(/*sr.RESULTS_RECOGNITION*/SpeechRecognizer.RESULTS_RECOGNITION) as java.util.ArrayList<String> //copy pasted from above
        println("partial results: $result")
    }

    //vague event I guess?
    override fun onEvent(eventType: Int, params: Bundle) {
        println("there's been an event")
    }

    //TODO: make a button that calls this when pressed (and changes colors...)
    fun startListening() {
        speechButton!!.setBackgroundTintList(ColorStateList.valueOf(Color.RED))
        //speechButton!!.setBackgroundColor(Color.RED)
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        sr!!.startListening(speechIntent)
    }
}
