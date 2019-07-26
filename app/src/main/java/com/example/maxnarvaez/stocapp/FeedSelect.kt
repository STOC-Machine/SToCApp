package com.example.maxnarvaez.stocapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.Log
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import android.os.AsyncTask
/** A class for transferring Message. */

import java.io.*

//class Message {
//
//    // class variables
//
//    /** a single character that indicates the end of each part of a Message when it is expressed as an array of byte  */
//    protected val TERMINATOR = ","
//
//    /** the buffer size for input  */
//    protected val MAXBUFF = 10000
//
//    // state variables
//
//    /** the type of the message  */
//    internal var type:String
//
//    /** content of the message  */
//    internal var content:String
//
//    // methods
//
//    /** Produce a byte-array representation of this Message
//     * @return an array of byte representing a Message
//     * @exception UnsupportedEncodingException
//     * Thrown if there is an error in encoding
//     */
//    val bytes:ByteArray
//        @Throws(UnsupportedEncodingException::class)
//        get() {
//            val str = "$type,$content"
//            return str.toByteArray()
//        }
//
//    // constructors
//
//    /** Initializes the two state variables to those String values
//     * @param str1 The type of the message
//     * @param str2 The content of the message
//     */
//    internal constructor(str1:String, str2:String) {
//        type = str1
//        content = str2
//    }
//
//    /** Attempts to read a Message represented as an array of byte initialize the two state variables from that array, using the delimiter TERMINATOR to determine where the two values end
//     * @param is InputStream which is associated with a socket
//     * @exception EOFException
//     * Thrown when encounter end of stream
//     * @exception IOExcetion
//     * Thrown when encounter other
//     */
//    @Throws(EOFException::class, IOException::class)
//    internal constructor(`is`:InputStream) {
//        val inBuff = ByteArray(MAXBUFF)
//        var count = 0  // to hold number of bytes read
//        try
//        {
//            count = `is`.read(inBuff)
//        }
//        catch (e:IOException) {
//            println(e.message)
//            System.exit(0)
//        }
//
//        val s = String(inBuff)
//        val str = s.split(TERMINATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        type = str[0]
//        content = str[1]
//        println(
//            "Successfully received the following " + count
//                    + " bytes:" + content
//        )
//        val tmp = this.toString()
//        // System.out.write(tmp.getBytes());
//        // System.out.println();
//    }
//
//    /** Produce a readable representation of this Message
//     * @return a String representing a printable version of the Message
//     */
//    override fun toString():String {
//        return "[type = $type, content = $content]"
//    }
//
//    /** Transmit this message on a stream, such as a socket output stream
//     * @param Stream on which to transmit the message, in UTF-8 encoding
//     * @exception IOException
//     * Throw if there is a problem with `str.write()`
//     */
//    @Throws(IOException::class)
//    fun send(str:OutputStream) {
//        val b = ("$type,$content").toByteArray()
//        str.write(b)
//    }
//
//    companion object {
//
//        /** Test for this class.
//         * @param input from terminal
//         */
//        @JvmStatic  fun main(args:Array<String>) {
//
//        }
//    }
//}
//
//
//
//
//
//
//class OpenSocketTask() : AsyncTask<Void, Void, Void>() {
//    init {
//
//    }
//
//    override fun doInBackground(vararg params: Void?): Void? {
//        println("Hi")
//        val client = Socket("10.42.235.221", 12459)
//        println("Hi1")
//        // private val reader: Scanner = Scanner(client.getInputStream())
//        val writer: OutputStream = client.getOutputStream()
//
//        val m = Message("EOT", "Hi")
//        m.send(writer)
//        // private val calculator: Calculator = Calculator()
//        // private var running: Boolean = false
//
//
//        print("Test")
//
//        // Send message
//        writer.write(("Hi" + '\n').toByteArray(Charset.defaultCharset()))
//
//        // Closing
//        client.close()
//        return null
//    }
//}

//internal class OpenSocketTask : AsyncTask<Void, Void, Void>() {
//
////    private var exception: Exception? = null
//
//    protected override fun doInBackground() -> Void{
//
//    }
//
//    protected override fun onPostExecute() -> Void {
//        // TODO: check this.exception
//        // TODO: do something with the feed
//        println("Hi")
//        val client = Socket("10.42.235.221", 12459)
//        println("Hi1")
//        // private val reader: Scanner = Scanner(client.getInputStream())
//        val writer: OutputStream = client.getOutputStream()
//        // private val calculator: Calculator = Calculator()
//        // private var running: Boolean = false
//
//
//        print("Test")
//
//        // Send message
//        writer.write(("Hi" + '\n').toByteArray(Charset.defaultCharset()))
//
//        // Closing
//        client.close()
//    }
//}

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
//                println("Hiascasd12wd123i39f813");
//                OpenSocketTask().execute()
                openFeed(i);
            }
        }

        return thisView
    }

    private fun openFeed(feed: Int) {
        feedChoice = feed
        val intent = Intent(this.context, PiFeed::class.java)
        startActivity(intent)
    }

//    fun open_socket(){
//        println("Hi")
//        val client = Socket("10.42.235.221", 12459)
//        println("Hi1")
//        // private val reader: Scanner = Scanner(client.getInputStream())
//        val writer: OutputStream = client.getOutputStream()
//        // private val calculator: Calculator = Calculator()
//        // private var running: Boolean = false
//
//
//        print("Test")
//
//        // Send message
//        writer.write(("Hi" + '\n').toByteArray(Charset.defaultCharset()))
//
//        // Closing
//        client.close()
//    }

}
