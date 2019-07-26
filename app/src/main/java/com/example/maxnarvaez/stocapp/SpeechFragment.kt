package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.*
import java.net.Socket
import kotlin.system.exitProcess

class Message {

    // class variables

    /** a single character that indicates the end of each part of a Message when it is expressed as an array of byte  */
    private val terminator = ","

    /** the buffer size for input  */
    private val maxBuff = 10000

    // state variables

    /** the type of the message  */
    var type: String

    /** content of the message  */
    var content: String

    // methods

    /** Produce a byte-array representation of this Message
     * @return an array of byte representing a Message
     * @exception UnsupportedEncodingException
     * Thrown if there is an error in encoding
     */
    val bytes: ByteArray
        get() = "$type,$content".toByteArray()

    // constructors

    /** Initializes the two state variables to those String values
     * @param str1 The type of the message
     * @param str2 The content of the message
     */
    internal constructor(str1: String, str2: String) {
        type = str1
        content = str2
    }

    /** Attempts to read a Message represented as an array of byte initialize the two state variables from that array, using the delimiter terminator to determine where the two values end
     * @param is InputStream which is associated with a socket
     * @exception EOFException
     * Thrown when encounter end of stream
     * @exception IOException
     * Thrown when encounter other
     */
    internal constructor(`is`: InputStream) {
        val inBuff = ByteArray(maxBuff)
        var count: Int  // to hold number of bytes read
        try {
            count = `is`.read(inBuff)
        } catch (e: IOException) {
            println(e.message)
            exitProcess(0)
        }

        val s = String(inBuff)
        val str = s.split(terminator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        type = str[0]
        content = str[1]
        println(
            "Successfully received the following " + count
                    + " bytes:" + content
        )
//        val tmp = this.toString()
        // System.out.write(tmp.getBytes());
        // System.out.println();
    }

    /** Produce a readable representation of this Message
     * @return a String representing a printable version of the Message
     */
    override fun toString(): String {
        return "[type = $type, content = $content]"
    }

    /** Transmit this message on a stream, such as a socket output stream
     * @param str Stream on which to transmit the message, in UTF-8 encoding
     * @exception IOException
     * Throw if there is a problem with `str.write()`
     */
    fun send(str: OutputStream) {
        str.write("$type,$content".toByteArray())
    }

    companion object {

        /** Test for this class.
         * @param args input from terminal
         */
        @JvmStatic
        fun main(args: Array<String>) {

        }
    }
}


class OpenSocketTask() : AsyncTask<String, Void, Void>() {
    // class variables
    lateinit var client: Socket

    init {
//        println("Init socket")

    }

    override fun doInBackground(vararg mess: String?): Void? {
        // private val reader: Scanner = Scanner(client.getInputStream())
        client = Socket("10.42.235.221", 12459)

        val writer: OutputStream = client.getOutputStream()

        println(mess.javaClass.kotlin)

        val m = Message("EOT", mess[0].toString())
        m.send(writer)
        // private val calculator: Calculator = Calculator()
        // private var running: Boolean = false

        print("Send message ")
        println(mess)

        // Closing
        client.close()
        return null
    }
}


class SpeechFragment : Fragment(), RecognitionListener {
    lateinit var sr: SpeechRecognizer
    lateinit var speechText: TextView
    lateinit var speechButton: Button

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
        speechText = thisView.findViewById<View>(R.id.speechText) as TextView
        speechButton = thisView.findViewById<View>(R.id.speechButton) as Button
        speechButton.setOnClickListener {
            startListening()
        }

        speechButton.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
        speechText.setBackgroundColor(0xfffafafa.toInt())

        // Inflate the layout for this fragment
        return thisView
    }


    //App is ready for the user to speak
    override fun onReadyForSpeech(params: Bundle) {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
    }

    //user has begun to speak
    override fun onBeginningOfSpeech() {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.CYAN)
    }

    //change in volume (I believe, haven't read much on this)
    override fun onRmsChanged(rmsdB: Float) {
        //don't bother
    }

    //buffer received (was never triggered in all my testing)
    override fun onBufferReceived(buffer: ByteArray) {
        //don't bother
    }

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

        println("ERROR $error")
        speechText.text = errorString
        //sr!!.stopListening()
        //not sure if this is how you're supposed to do it, but it works!
        onBeginningOfSpeech()
        onEndOfSpeech()
    }

    //speech has been processed, here are the results
    override fun onResults(results: Bundle) {
        val result = mutableListOf<String>().apply {
            (results.get(SpeechRecognizer.RESULTS_RECOGNITION) as List<*>).forEach {
                this.add(it as String)
            }
        }
        speechText.text = result[0]//Main one, the one it's most confident about...
        println("Possible results: $result")

//        val data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//        val word = data.get(data.size() - 1) as String
//        recognisedText.setText(word)
//        val client = Socket("10.42.235.221", 12459)
        OpenSocketTask().execute(result[0])

    }

    //partial results? I never ran into these but maybe if it was interrupted?
    override fun onPartialResults(partialResults: Bundle) {
        val result = mutableListOf<String>().apply {
            (partialResults.get(SpeechRecognizer.RESULTS_RECOGNITION) as List<*>).forEach {
                this.add(it as String)
            }
        } //copy pasted from above
        println("partial results: $result")
    }

    //vague event I guess?
    override fun onEvent(eventType: Int, params: Bundle) {
        println("there's been an event")
    }

    //TODO: make a button that calls this when pressed (and changes colors...)
    private fun startListening() {
        speechButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        sr.startListening(speechIntent)
    }
}
