package com.example.maxnarvaez.stocapp

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class Message {

    // class variables

    /** a single character that indicates the end of each part of a Message when it is expressed as an array of byte  */
    private val terminator = ","

    /** the buffer size for input  */
    private val maxBuff = 10000

    // state variables

    /** the type of the message  */
    private var type: String

    /** content of the message  */
    private var content: String

    // methods

    /** Produce a byte-array representation of this Message
     * @return an array of byte representing a Message
     * Thrown if there is an error in encoding
     */
    val bytes: ByteArray
        get() = "$type,$content".toByteArray()

    // constructors

    /** Initializes the two state variables to those String values
     * @param str1 The type of the message
     * @param str2 The content of the message
     */
    constructor(str1: String, str2: String) {
        type = str1
        content = str2
    }

    /** Attempts to read a Message represented as an array of byte initialize the two state variables from that array, using the delimiter terminator to determine where the two values end
     * @param iStream InputStream which is associated with a socket
     */
    constructor(iStream: InputStream) {
        try {
            val inBuff = ByteArray(maxBuff)
            val count = iStream.read(inBuff)
            print(inBuff)

            val s = String(inBuff)
            val str = s.split(terminator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val tmp = str[1].split(" ").dropLastWhile { it.isEmpty() }.toTypedArray()
            content = tmp[0]
            type = str[0]
            Log.d(
                "Parser Comms",
                "Successfully received the following " + content
            )

        } catch (e: IOException) {
            Log.e("Parser Comms", e.message ?: "")
            throw e
        }
    }

    /** Produce a readable representation of this Message
     * @return a String representing a printable version of the Message
     */
    override fun toString(): String = "[type = $type, content = $content]"

    fun getContent(): String{
        return content
    }

    /** Transmit this message on a stream, such as a socket output stream
     * @param str Stream on which to transmit the message, in UTF-8 encoding
     * @exception IOException
     * Throw if there is a problem with `str.write()`
     */
    fun send(str: OutputStream) {
        str.write("$type,$content".toByteArray())
    }
}