package com.example.maxnarvaez.stocapp

import android.os.AsyncTask
import android.util.Log
import java.io.OutputStream
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException


class OpenSocketTask : AsyncTask<String, Void, Void>() {
    // class variables
    private lateinit var client: Socket

    override fun doInBackground(vararg mess: String?): Void? {
        // private val reader: Scanner = Scanner(client.getInputStream())
        try {
            Log.d("Parser Send", "Connecting to $parserIP")
            client = Socket().apply {
                connect(InetSocketAddress(parserIP, 12459), 2000)
            }
            Log.d("Parser Send", "Connected to $parserIP")
        } catch (e: ConnectException) {
            Log.e(
                "Parser Send",
                "Could not connect to $parserIP: cause=ConnectException"
            )
            return null
        } catch (e: SocketTimeoutException) {
            Log.e(
                "Parser Send",
                "Could not connect to $parserIP: cause=SocketTimeoutException"
            )
            return null
        }
        val writer: OutputStream = client.getOutputStream()

        Log.d("Parser Send", mess.javaClass.kotlin.toString())

        Message("EOT", mess[0].toString()).send(writer)
        // private val calculator: Calculator = Calculator()
        // private var running: Boolean = false

//        print("Send message ")
//        println(mess)

        // Closing
        client.close()
        return null
    }
}