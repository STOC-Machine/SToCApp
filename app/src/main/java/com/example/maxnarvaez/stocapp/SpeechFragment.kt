package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


class SpeechFragment : Fragment(), RecognitionListener {
    private lateinit var sr: SpeechRecognizer
    private lateinit var speechText: TextView
    private lateinit var speechButton: Button
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sr = SpeechRecognizer.createSpeechRecognizer(this.context).apply {
            setRecognitionListener(this@SpeechFragment) //this activity is the listener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val thisView = inflater.inflate(R.layout.fragment_speech, container, false)
        speechText = thisView.findViewById(R.id.speechText) as TextView
        speechButton = thisView.findViewById(R.id.speechButton) as Button
        speechButton.setOnClickListener {
            startListening()
        }
        sendButton = thisView.findViewById(R.id.emergencyReset) as Button
        sendButton.setOnClickListener {
            sr.stopListening()
            sr.destroy()
            sr.setRecognitionListener(this)
            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            sr.startListening(speechIntent)
        }

        speechButton.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
        sendButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
        speechText.setBackgroundColor(0xfffafafa.toInt())

        // Inflate the layout for this fragment
        return thisView
    }


    //App is ready for the user to speak
    override fun onReadyForSpeech(params: Bundle) {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
        sendButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
    }

    //user has begun to speak
    override fun onBeginningOfSpeech() {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.CYAN)
    }

    //change in volume (I believe, haven't read much on this)
    override fun onRmsChanged(rmsdB: Float) {}

    //buffer received (was never triggered in all my testing)
    override fun onBufferReceived(buffer: ByteArray) {}

    //it's been a while since they talked, they're done
    override fun onEndOfSpeech() {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
    }

    //something went wrong
    override fun onError(error: Int) {
        val errorString: String = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "ERROR_AUDIO"
            SpeechRecognizer.ERROR_CLIENT -> "ERROR_CLIENT"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "ERROR+INSUFFICIENT_PERMISSIONS"
            SpeechRecognizer.ERROR_NETWORK -> "ERROR_NETWORK"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "ERROR_NETWORK_TIMEOUT"
            SpeechRecognizer.ERROR_NO_MATCH -> "ERROR_NO_MATCH"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "ERROR_RECOGNIZER_BUSY"
            SpeechRecognizer.ERROR_SERVER -> "ERROR_SERVER"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "ERROR_SPEECH_TIMEOUT"
            else -> {
                "ERROR_IF_YOU_SEE_THIS_SOMETHING_HAS_GONE_HORRIBLY_WRONG"
            }
        }

        speechText.text = errorString
        Log.e("Speech", "ERROR $error")
        //not sure if this is how you're supposed to do it, but it works!
        onBeginningOfSpeech()
        onEndOfSpeech()
        if (error==6) Thread.sleep(100)
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        sr.startListening(speechIntent)
    }

    //speech has been processed, here are the results
    override fun onResults(results: Bundle) {
        val result = mutableListOf<String>().apply {
            (results.get(SpeechRecognizer.RESULTS_RECOGNITION) as List<*>).forEach {
                this.add(it as String)
            }
        }
        val resultList = if (result.size >= 2) listOf(result[0], result[1]) else listOf(result[0])
        speechText.text = resultList.toString() //Main one, the one it's most confident about...
        Log.d("Speech", "Possible results: $result")
        sendButton.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
        var send = false
        result.forEach {
            if (it.contains("send", true) || it.contains("done", false)) sendMessage()
        }
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        sr.startListening(speechIntent)
//        val data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//        val word = data.get(data.size() - 1) as String
//        recognisedText.setText(word)
//        val client = Socket("10.42.235.221", 12459)
    }

    //partial results? I never ran into these but maybe if it was interrupted?
    override fun onPartialResults(partialResults: Bundle) {}

    //vague event I guess?
    override fun onEvent(eventType: Int, params: Bundle) {}

    private fun startListening() {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
        speechButton.setOnClickListener { }
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        sr.startListening(speechIntent)
    }

    private fun sendMessage() {
        Log.d("Speech", "Sending ${speechText.text}")
        OpenSocketTask().execute(speechText.text.toString())
    }
}
