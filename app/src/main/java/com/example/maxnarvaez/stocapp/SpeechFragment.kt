package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_speech.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SpeechFragment : Fragment(), RecognitionListener {
    private lateinit var sr: SpeechRecognizer
    private lateinit var speechText: TextView
    private lateinit var speechButton: Button
    private lateinit var stopSpeechButton: Button
    private lateinit var status: TextView
    private lateinit var buttonStatusContainer: FrameLayout
    private lateinit var sentStatusText: TextView
    private var stopListening = false
    private lateinit var speechTexts: List<String>

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
        stopSpeechButton = thisView.findViewById(R.id.stopButton) as Button
        buttonStatusContainer = thisView.findViewById(R.id.button_status_container) as FrameLayout
        sentStatusText = thisView.findViewById(R.id.sent_status) as TextView
        speechButton.setOnClickListener {
            if (sendTriggers.isNotEmpty()) {
                val ret = startListening()
                if (ret == 1)
                    Toast.makeText(this.context, "Could not connect to parser", Toast.LENGTH_SHORT)
                        .show()
            } else {
                Toast.makeText(this.context, "No send triggers selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        stopSpeechButton.setOnClickListener {
            stopListening()
        }
        speechTexts = listOf(
            getString(R.string.speech_text_1),
            getString(R.string.speech_text_2),
            getString(R.string.speech_text_3),
            getString(R.string.speech_text_4),
            getString(R.string.speech_text_5),
            getString(R.string.speech_text_6)
        )
        speechText.text = speechTexts.random()

        speechButton.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
        stopSpeechButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
        speechText.setBackgroundColor(0xfffafafa.toInt())
        sentStatusText.setBackgroundColor(0xfffafafa.toInt())

        // Inflate the layout for this fragment
        return thisView
    }


    //App is ready for the user to speak
    override fun onReadyForSpeech(params: Bundle) {
        status.setBackgroundColor(Color.GREEN)
        status.text = getString(R.string.waiting_status)
    }

    //user has begun to speak
    override fun onBeginningOfSpeech() {
        status.setBackgroundColor(Color.CYAN)
        status.text = getString(R.string.listening_status)
    }

    //change in volume (I believe, haven't read much on this)
    override fun onRmsChanged(rmsdB: Float) {}

    //buffer received (was never triggered in all my testing)
    override fun onBufferReceived(buffer: ByteArray) {}

    //it's been a while since they talked, they're done
    override fun onEndOfSpeech() {
        status.setBackgroundColor(Color.GRAY)
        status.text = getString(R.string.processing_status)
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

//        speechText.text = errorString
//        sentStatusText.text = getString(R.string.no_message_status)
        Log.e("Speech", "ERROR $error")
        //not sure if this is how you're supposed to do it, but it works!
//        onBeginningOfSpeech()
//        onEndOfSpeech()
        if (error == 6) Thread.sleep(100)
        if (error == 8) stopListening()
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        if (!stopListening) sr.startListening(speechIntent)
    }

    //speech has been processed, here are the results
    override fun onResults(results: Bundle) {
        val result = mutableListOf<String>().apply {
            (results.get(SpeechRecognizer.RESULTS_RECOGNITION) as List<*>).forEach {
                this.add(it as String)
            }
        }
        val resultList = if (result.size >= 2) listOf(result[0], result[1]) else listOf(result[0])
        Log.d("Speech", "Possible results: $result")
        var sent = false
        message@ for (m in result) {
            for (t in sendTriggers) {
                if (m.contains(t, true)) {
                    sent = true
                    sentStatusText.text = getString(R.string.sent_status)
                    sendMessage()
                    break@message
                }
            }
        }
        if (!sent) {
            sentStatusText.text = getString(R.string.no_trigger_status)
            speechText.text = resultList.toString()
            sentStatusText.text = getString(R.string.send_message_status)
        }
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        if (!stopListening) sr.startListening(speechIntent)
//        val data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//        val word = data.get(data.size() - 1) as String
//        recognisedText.setText(word)
//        val client = Socket("10.42.235.221", 12459)
    }

    //partial results? I never ran into these but maybe if it was interrupted?
    override fun onPartialResults(partialResults: Bundle) {}

    //vague event I guess?
    override fun onEvent(eventType: Int, params: Bundle) {}

    private fun startListening(): Int {
        stopListening = false
        val connected = runBlocking {
            return@runBlocking ParserConnection.connect()
        }
        if (!connected) return 1
        status = TextView(speechButton.context)
        buttonStatusContainer.removeView(speechButton)
        buttonStatusContainer.addView(status)
        status.text = getString(R.string.waiting_status)
        status.gravity = Gravity.CENTER_HORIZONTAL
        status.textSize = 30.0F
        status.setBackgroundColor(Color.GREEN)
        stopButton.visibility = View.VISIBLE

        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        sr.startListening(speechIntent)
        return 0
    }

    private fun stopListening() {
        stopListening = true
        sr.stopListening()
        runBlocking { ParserConnection.disconnect() }
        if (this::status.isInitialized)
            buttonStatusContainer.removeView(status)
        buttonStatusContainer.removeView(speechButton)
        buttonStatusContainer.addView(speechButton)
        try {
            stopButton.visibility = View.INVISIBLE
        } catch (e: IllegalStateException) {
        }
        speechText.text = speechTexts.random()
    }

    private fun sendMessage() {
        Log.d("Speech", "Sending ${speechText.text}")
        GlobalScope.launch {
            ParserConnection.send(speechText.text.toString())
        }
    }

    override fun onDetach() {
        stopListening()
        super.onDetach()
    }
}
