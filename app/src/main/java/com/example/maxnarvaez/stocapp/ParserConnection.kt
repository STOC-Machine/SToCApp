package com.example.maxnarvaez.stocapp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket


object ParserConnection {

    private lateinit var socket: Socket
    private lateinit var oStream: OutputStream
    private var connected = false
    val isConnected: Boolean
        get() = connected

    suspend fun connect(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                socket = Socket()
                socket.connect(InetSocketAddress(parserIP, parserPort), 5000)
                connected = true
                oStream = socket.getOutputStream()
            }
            true
        } catch (e: Exception) {
            Log.e("Parser Connection", "Connect failed: $e")
            connected = false
            false
        }
    }

    suspend fun send(message: String) {
        if (!connected) {
            Log.e("Parser Connection", "Not Connected")
            return
        }
        withContext(Dispatchers.IO) {
            Message("EOT", message).send(oStream)
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            socket.close()
        }
    }
}


//class OpenSocketTask : AsyncTask<String, Void, Void>() {
//    // class variables
//    private lateinit var client: Socket
//
//    override fun doInBackground(vararg mess: String?): Void? {
//        // private val reader: Scanner = Scanner(client.getInputStream())
//        try {
//            Log.d("Parser Send", "Connecting to $parserIP")
//            client = Socket().apply {
//                connect(InetSocketAddress(parserIP, 12459), 2000)
//            }
//            Log.d("Parser Send", "Connected to $parserIP")
//        } catch (e: ConnectException) {
//            Log.e(
//                "Parser Send",
//                "Could not connect to $parserIP: cause=ConnectException"
//            )
//            return null
//        } catch (e: SocketTimeoutException) {
//            Log.e(
//                "Parser Send",
//                "Could not connect to $parserIP: cause=SocketTimeoutException"
//            )
//            return null
//        }
//        val writer: OutputStream = client.getOutputStream()
//
//        Log.d("Parser Send", mess.javaClass.kotlin.toString())
//
//        Message("EOT", mess[0].toString()).send(writer)
//        // private val calculator: Calculator = Calculator()
//        // private var running: Boolean = false
//
////        print("Send message ")
////        println(mess)
//
//        // Closing
//        client.close()
//        return null
//    }
//}